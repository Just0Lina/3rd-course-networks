package bin.SocketServer;//package edu.school21.sockets.app;

//cd /home/alina/3rd\ course/Сети/lab__2/src/main/java/bin/SocketServer/
//javac bin/SocketServer/Main.java && java bin.SocketServer.Main --server-port=8084

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;


public class Main {
    static String uploadFolder = "uploads";

    private static int checkArgs(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--server-port=")) {
            System.err.println("Wrong parameters. Try --server-port=");
            System.exit(-1);
        }
        return Integer.parseInt(args[0].substring("--server-port=".length()));
    }

    public static void main(String[] args) {
        int port = checkArgs(args);
        System.out.println("Started Server");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер ожидает подключения на порту " + port);


            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Подключение клиента: " + clientSocket.getInetAddress().getHostAddress());

                    // Создаем новый поток для обработки клиента
                    Thread clientThread = new Thread(new ClientHandler(clientSocket));
                    clientThread.start();
                    clientThread.join();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            Map<Socket, Long> startTimeMap = new HashMap<>();
            // Создаем потоки для чтения данных от клиента
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Читаем имя файла
            String fileName = null;
            try {
                fileName = reader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            // Читаем размер файла
            int fileSize = 0;
            try {
                fileSize = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String uploadFolderPath = new File("").getAbsolutePath() + File.separator + "bin/SocketServer" + File.separator + uploadFolder;
            System.out.println(uploadFolderPath);
            // Создаем папку "uploads", если она не существует
            File uploadDirectory = new File(uploadFolderPath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            // Формируем полный путь к файлу, включая "uploads" и имя файла
            String filePath = uploadFolderPath + File.separator + fileName;
            // Сохраняем файл на сервере
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                long startTime = System.currentTimeMillis();
                long totalBytes = 0;
                boolean flag = true;

                while (flag && (bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
                    fileOut.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                    long currentTime = System.currentTimeMillis();

                    if (currentTime - startTimeMap.getOrDefault(clientSocket, startTime) >= 300) {
                        double speed = ((double) bytesRead / 1024) / ((currentTime - startTime) / 1000.0);
                        double avgSpeed = ((double) totalBytes / 1024) / ((currentTime - startTimeMap.getOrDefault(clientSocket, startTime)) / 1000.0);

                        System.out.println("Скорость передачи для клиента " + clientSocket.getInetAddress().getHostAddress() +
                                ": Мгновенная: " + speed + " KB/сек, Средняя: " + avgSpeed + " KB/сек");
                        startTimeMap.put(clientSocket, currentTime);

                    }
                    if (bytesRead != 1024) flag = false;
                }

                long endTime = System.currentTimeMillis();
                double avgSpeed = ((double) totalBytes / 1024) / ((endTime - startTimeMap.getOrDefault(clientSocket, startTime)) / 1000.0);
                double endSpeed = ((double) totalBytes / 1024) / ((endTime - startTime) / 1000.0);
                fileOut.close();

                System.out.println("Файл " + fileName + " успешно принят и сохранен в папке " + uploadFolder);
                System.out.println("Скорость передачи для клиента " + clientSocket.getInetAddress().getHostAddress() +
                        ": Мгновенная: " + endSpeed + " KB/сек, Средняя: " + avgSpeed + " KB/сек");
                if (totalBytes == fileSize) {
                    // Сообщаем клиенту об успехе
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
                    writer.println("Файл успешно передан\n");
                    writer.flush();
                } else {
                    // Сообщаем клиенту о неуспешной передаче
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
                    writer.println("Ошибка передачи файла\n");
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }
}
