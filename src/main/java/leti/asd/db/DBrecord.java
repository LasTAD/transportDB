package leti.asd.db;

/**
 * Created by nikolaikobyzev on 15.10.16.
 */
public class DBrecord implements Comparable{
    private String full_name;
    private int level;
    private int years_work;
    private int salary;

    public int compareTo(Object o) {
        return 0;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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
