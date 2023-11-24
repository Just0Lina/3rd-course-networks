
import java.util.*;

public class Copies {
    private Map<UUID, Long> lastTimeCopies = new HashMap<>();
    private Map<UUID, String> copies = new HashMap<>();

    public synchronized void deleteCopies() {
        int copiesAmount = copies.size();
        List<UUID> toRemove = new ArrayList<>();

        for (Map.Entry<UUID, Long> uuidLongEntry : lastTimeCopies.entrySet()) {
            if (System.currentTimeMillis() - uuidLongEntry.getValue() > 8000) {
                toRemove.add(uuidLongEntry.getKey());
                copies.remove(uuidLongEntry.getKey());
                System.out.println("Removing..");
            }
        }
        for (UUID uuid : toRemove) {
            lastTimeCopies.remove(uuid);
        }
        toRemove.clear();
        int copiesAmountAfter = copies.size();
        if (copiesAmountAfter != copiesAmount) {
            this.printCopies();
        }
    }

    public synchronized void putId(UUID uuid, String addr) {
        int copiesAmount = copies.size();
        if (!copies.containsKey(uuid)) {
            copies.put(uuid, addr);
        }
        lastTimeCopies.put(uuid, System.currentTimeMillis());
        int copiesAmountAfter = copies.size();
        if (copiesAmountAfter > copiesAmount) {
            this.printCopies();
        }
    }

    public synchronized void printCopies() {
        System.out.println(copies.size());
        for (Map.Entry<UUID, String> uuidExist : copies.entrySet()) {
            System.out.println("Copy - " + uuidExist.getValue()) ;
        }
    }

}
