package leti.asd.db.db_list;

/**
 * Project transportDB
 * Created by nikolaikobyzev on 15.10.16.
 */
public class DBrecord {
    private String fullName;
    private int level;
    private int years_work;
    private int salary;

    public DBrecord() {

    }

    public DBrecord(String fullName, int level, int years_work, int salary) {
        this.fullName = fullName;
        this.level = level;
        this.years_work = years_work;
        this.salary = salary;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getYears_work() {
        return years_work;
    }

    public void setYears_work(int years_work) {
        this.years_work = years_work;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }


}
