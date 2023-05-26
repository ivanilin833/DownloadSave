import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    private static final String basePath = "D:\\Games\\savegames\\";
    private static List<String> nameSaves = new ArrayList<>(3);

    public static void main(String[] args) {
        openZip(basePath + "save.zip", basePath);

        nameSaves.forEach(x -> System.out.println(openProgress(basePath + x)));
    }

    private static void openZip(String pathToZip, String pathToSave){
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(pathToZip))) {
            ZipEntry zipEntry;
            String name;

            while ((zipEntry = zis.getNextEntry()) != null){
                name = zipEntry.getName();
                nameSaves.add(name);
                try(FileOutputStream fos = new FileOutputStream(pathToSave + name)){
                    for(int c = zis.read(); c != -1; c = zis.read()){
                        fos.write(c);
                    }
                    fos.flush();
                    zis.closeEntry();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static GameProgress openProgress(String pathToSave){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathToSave))) {
            return (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
