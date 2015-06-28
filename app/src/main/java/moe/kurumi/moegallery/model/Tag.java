package moe.kurumi.moegallery.model;

import java.util.Comparator;

/**
 * Created by kurumi on 15-6-1.
 */
public interface Tag {

    String toString();

    String getName();

    Long getCount();


    class TagComparator implements Comparator<Tag> {

        public int compare(Tag arg0, Tag arg1) {
            int flag = arg1.getCount().compareTo(arg0.getCount());
            if (flag == 0) {
                return arg1.getName().compareTo(arg0.getName());
            } else {
                return flag;
            }
        }

    }

}
