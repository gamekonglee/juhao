package bc.juhao.com.bean;

import java.io.Serializable;

/**
 * Created by gamekonglee on 2018/3/30.
 */

public class BankInfo  implements Serializable{
    /**
     * Copyright 2018 bejson.com
     */
    /**
     * Auto-generated: 2018-03-30 14:27:58
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */

        private int uid;
        private String company;
        private String bank;
        private String account;
        private String name;
        private String phone;
        public void setUid(int uid) {
            this.uid = uid;
        }
        public int getUid() {
            return uid;
        }

        public void setCompany(String company) {
            this.company = company;
        }
        public String getCompany() {
            return company;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }
        public String getBank() {
            return bank;
        }

        public void setAccount(String account) {
            this.account = account;
        }
        public String getAccount() {
            return account;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getPhone() {
            return phone;
        }

}
