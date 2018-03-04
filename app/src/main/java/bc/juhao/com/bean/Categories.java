package bc.juhao.com.bean;

import java.util.List;

/**
 * Created by bocang on 18-2-6.
 */

public class Categories {
    /**
     * Copyright 2018 bejson.com
     */

    /**
     * Auto-generated: 2018-02-06 14:42:21
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

        private int id;
        private String name;
        private String desc;
        private String thumbs;
        private String photo;
        private int more;
        private List<Object> categories;
        private int sort;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
        public String getDesc() {
            return desc;
        }

        public void setThumbs(String thumbs) {
            this.thumbs = thumbs;
        }
        public String getThumbs() {
            return thumbs;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
        public String getPhoto() {
            return photo;
        }

        public void setMore(int more) {
            this.more = more;
        }
        public int getMore() {
            return more;
        }

        public void setCategories(List<Object> categories) {
            this.categories = categories;
        }
        public List<Object> getCategories() {
            return categories;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
        public int getSort() {
            return sort;
        }

}
