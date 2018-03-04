package bc.juhao.com.bean;

import java.util.List;

/**
 * Created by bocang on 18-2-6.
 */

public class CategoriesBean {
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
        private Photo photo;
        private int more;
        private List<Categories> categories;
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

        public void setPhoto(Photo photo) {
            this.photo = photo;
        }
        public Photo getPhoto() {
            return photo;
        }

        public void setMore(int more) {
            this.more = more;
        }
        public int getMore() {
            return more;
        }

        public void setCategories(List<Categories> categories) {
            this.categories = categories;
        }
        public List<Categories> getCategories() {
            return categories;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
        public int getSort() {
            return sort;
        }
/**
 * Copyright 2018 bejson.com
 */

    /**
     * Auto-generated: 2018-02-06 14:42:21
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Photo {

        private String width;
        private String height;
        private String thumb;
        private String large;

        public void setWidth(String width) {
            this.width = width;
        }

        public String getWidth() {
            return width;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getHeight() {
            return height;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getThumb() {
            return thumb;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getLarge() {
            return large;
        }
    }
}
