package bin.SocketClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

//cd /home/alina/3rd\ course/Сети/lab__2/src/main/java/bin/SocketClient/
//  javac bin/SocketClient/Main.java && java bin.SocketClient.Main --directory=/home/alina/Downloads/gold_advanced_coursebook_with_2015_exam_specifications_audio.zip --ip-address="localhost" --port=8084
//  javac bin/SocketClient/Main.java && java bin.SocketClient.Main --directory=/home/alina/Downloads/3\ курс.rar --ip-address="127.0.0.2" --port=8084
public class Main {
    private static void checkArgs(String[] args) {
        if (args.length != 3 || !args[0].startsWith("--directory=") || !args[1].startsWith("--ip-address=") || !args[2].startsWith("--port=")) {
            System.err.println("Wrong parameters. Try --directory= --ip-address= --port= ");
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        checkArgs(args);
        String filename = args[0].substring("--directory=".length());
        int port = Integer.parseInt(args[2].substring("--port=".length()));
        String ip = args[1].substring("--ip-address=".length());
        System.out.println("Started client");

        try (Socket clientSocket = new Socket(ip, port)) {
            // Создаем потоки для отправки данных на сервер
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
            File file = new File(filename);
            long fileSize = file.length();

            // Отправляем имя файла
            writer.println(filename.substring(filename.lastIndexOf('/') + 1));
            // Отправляем размер файла
            System.out.println(filename.substring(filename.lastIndexOf('/') + 1) + " " + fileSize);
            writer.println(fileSize);
            writer.flush();
//
            // Отправляем содержимое файла
            byte[] fileData = new byte[(int) fileSize]; // Здесь загружаем содержимое файла в байтовый массив
            try (FileInputStream fis = new FileInputStream(file)) {
                int bytesRead = fis.read(fileData); // Читаем содержимое файла в массив
                if (bytesRead == fileSize) {
                    System.out.println("Файл успешно прочитан в байтовый массив.");
                } else {
                    System.out.println("Произошла ошибка при чтении файла.");
                }
                OutputStream out = clientSocket.getOutputStream();
                out.write(fileData);
                out.flush();
            }

            System.out.println("Файл отпрален на сервер.");


            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Обработка ответа
            System.out.println("Ответ от сервера: " + response.toString());

        } catch (
                UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (
                FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (
                UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }


    }
}