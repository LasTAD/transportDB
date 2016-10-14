package leti.asd.db;

/**
 * Created by nikolaikobyzev on 14.10.16.
 */
public class DBrecord {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBrecord dBrecord = (DBrecord) o;

        if (level != dBrecord.level) return false;
        if (years_work != dBrecord.years_work) return false;
        if (salary != dBrecord.salary) return false;
        return full_name.equals(dBrecord.full_name);

    }

    @Override
    public int hashCode() {
        int result = full_name.hashCode();
        result = 31 * result + level;
        result = 31 * result + years_work;
        result = 31 * result + salary;
        return result;
    }

    private String full_name;
    private int level;
    private int years_work;
    private int salary;

    public String getFull_name() {
        return full_name;
    }

    public int getLevel() {
        return level;
    }

    public int getYears_work() {
        return years_work;
    }

    public int getSalary() {
        return salary;
    }
}
