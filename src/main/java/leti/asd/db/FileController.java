package leti.asd.db;

import leti.asd.db.db_list.DBrecord;
import leti.asd.db.db_list.ListDB;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * Project transportDB
 * Created by nikolaikobyzev on 07.11.16.
 */
class FileController {

    public static void saveToFile(ListDB listDB) throws IOException {
        FileOutputStream fileOut = new FileOutputStream("transportDB");
        DataOutputStream out = new DataOutputStream(fileOut);
        out.writeInt(listDB.size());
        for(DBrecord rec : listDB) {
            out.writeUTF(rec.getFullName());
            out.writeInt(rec.getLevel());
            out.writeInt(rec.getYears_work());
            out.writeInt(rec.getSalary());
        }
        out.close();
    }

    public static void loadFromFile(ListDB listDB) throws IOException {
        FileInputStream fileIn = new FileInputStream("transportDB");
        DataInputStream in = new DataInputStream(fileIn);
        int i = in.readInt();
        while (i > 0) {
            DBrecord rec = new DBrecord();
            rec.setFullName(in.readUTF());
            rec.setLevel(in.readInt());
            rec.setYears_work(in.readInt());
            rec.setSalary(in.readInt());
            listDB.add(rec);
            --i;
        }
        in.close();
    }

    public static void loadFromTextFile(ListDB listDB, String name) throws IOException {
        List<String> lines = Files.readAllLines(new File(name).toPath());
        for(String line : lines) {
            String[] data = line.split("\\|");
            if(data.length<1) continue;
            DBrecord rec = new DBrecord();
            rec.setFullName(data[0]);
            if(data.length < 2) {
                listDB.add(rec);
                continue;
            }

            int level = 0;
            try {
                level = Integer.parseInt(data[1]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка чтения разряда!");
            }
            rec.setLevel(level);

            int years_work = 0;
            try {
                years_work = Integer.parseInt(data[2]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка чтения стажа!");
            }
            rec.setYears_work(years_work);

            int salary = 0;
            try {
                salary = Integer.parseInt(data[3]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка чтения зарплаты!");
            }
            rec.setSalary(salary);
            listDB.add(rec);
        }
    }
}
