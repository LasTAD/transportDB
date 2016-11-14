package leti.asd.db.db_list;

import java.util.*;

/**
 * Created by nikolaikobyzev on 15.10.16.
 */
public class ListDB extends ArrayList<DBrecord>{

    @Override
    public void sort(Comparator<? super DBrecord> c) {
        for(int i = 0; i < this.size(); i++){
            DBrecord temp = this.get(i);
            int j = i - 1;
            while((j >= 0) && c.compare(this.get(j),temp) > 0){
                this.set(j + 1,this.get(j));
                j--;
            }
            this.set(j + 1, temp);
        }

    }

    public int searchRec(Object value, DBRecordField field){
        switch(field){
            case LEVEL:
                return searchWrap((int)value, DBRecordField.LEVEL);
            case YEARS_WORK:
                return searchWrap((int)value, DBRecordField.YEARS_WORK);
            case SALARY:
                return searchWrap((int)value, DBRecordField.SALARY);
            case FULLNAME:
                return searchWrap((String)value);
        }
        return -1;
    }

    //для цифровых полей
    private  int searchWrap(int key, DBRecordField field){
        if (this.size() == 0)
            return -1;

        boolean descendingOrder = this.get(0).getLevel() > this.get(this.size()-1).getLevel();
        return search(descendingOrder, key, 0, this.size(), field);
    }

    //для строк
    private  int searchWrap(String key){
        if (this.size() == 0)
            return -1;

        boolean descendingOrder = this.get(0).getLevel() > this.get(this.size()-1).getLevel();
        return search(descendingOrder, key, 0, this.size());
    }

    //для цифровых полей
    private  int search(boolean descendingOrder, int key, int left, int right, DBRecordField field) {
        int mid = left + (right - left) / 2;

        if (left >= right)
            return -(1 + left);

        if (this.get(left).getLevel() == key)
            return left;

        switch (field) {
            case LEVEL:
                if (this.get(left).getLevel() == key)
                    return left;

                if (this.get(mid).getLevel() == key) {
                    if (mid == left + 1)
                        return mid;
                    else
                        return search(descendingOrder, key, left, mid + 1, field);
                } else if ((this.get(mid).getLevel() > key) ^ descendingOrder)
                    return search(descendingOrder, key, left, mid, field);
                else
                    return search(descendingOrder, key, mid + 1, right, field);
            case YEARS_WORK:
                if (this.get(left).getYears_work() == key)
                    return left;

                if (this.get(mid).getYears_work() == key) {
                    if (mid == left + 1)
                        return mid;
                    else
                        return search(descendingOrder, key, left, mid + 1, field);
                } else if ((this.get(mid).getYears_work() > key) ^ descendingOrder)
                    return search(descendingOrder, key, left, mid, field);
                else
                    return search(descendingOrder, key, mid + 1, right, field);
            case SALARY:
                if (this.get(left).getSalary() == key)
                    return left;

                if (this.get(mid).getSalary() == key) {
                    if (mid == left + 1)
                        return mid;
                    else
                        return search(descendingOrder, key, left, mid + 1, field);
                } else if ((this.get(mid).getSalary() > key) ^ descendingOrder)
                    return search(descendingOrder, key, left, mid, field);
                else
                    return search(descendingOrder, key, mid + 1, right, field);
        }
        return -1;
    }

    //для строк
    private  int search(boolean descendingOrder, String key, int left, int right){

        int mid = left + (right - left) / 2;

        if (left >= right)
            return -(1 + left);

        if (key.equals(this.get(left).getFullName()))
            return left;

        if (this.get(left).getFullName() == key)
            return left;

        if (this.get(mid).getFullName() == key) {
            if (mid == left + 1)
                return mid;
            else
                return search(descendingOrder, key, left, mid + 1);
        }
        else if ((this.get(mid).getFullName().compareToIgnoreCase(key) == -1) ^ descendingOrder)
            return search(descendingOrder, key, left, mid);
        else
            return search(descendingOrder, key, mid + 1, right);
    }
}
