package bc.juhao.com.bean;

import java.util.List;

/**
 * Created by bocang on 18-2-6.
 */

public class AttrBean {
    /**
     * Copyright 2018 bejson.com
     */

    /**
     * Auto-generated: 2018-02-06 13:59:9
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

        private String filter_attr_name;
        private int index;
        private List<Attr_list> attr_list;
        public void setFilter_attr_name(String filter_attr_name) {
            this.filter_attr_name = filter_attr_name;
        }
        public String getFilter_attr_name() {
            return filter_attr_name;
        }

        public void setIndex(int index) {
            this.index = index;
        }
        public int getIndex() {
            return index;
        }

        public void setAttr_list(List<Attr_list> attr_list) {
            this.attr_list = attr_list;
        }
        public List<Attr_list> getAttr_list() {
            return attr_list;
        }
/**
 * Copyright 2018 bejson.com
 */

    /**
     * Auto-generated: 2018-02-06 13:59:9
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Attr_list {

        private String attr_value;
        private int id;
        public void setAttr_value(String attr_value) {
            this.attr_value = attr_value;
        }
        public String getAttr_value() {
            return attr_value;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

    }
}
