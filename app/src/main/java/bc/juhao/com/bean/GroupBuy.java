package bc.juhao.com.bean;

import java.util.List;

/**
 * Created by DEMON on 2018/1/25.
 */
public class GroupBuy {

    /**
     * id : 29
     * desc :
     * end_time : 2018-01-31 00:00
     * goods_id : 2284
     * name : 钜豪南山初雨普洱生茶
     * ext_info : {"deposit":0,"price_ladder":[{"amount":1,"price":100}],"buy_amount":0,"gift_integral":0,"restrict_amount":98}
     * start_time : 2018-01-13 00:00
     * number : 2
     * type : 1
     * is_finished : 0
     */
    private int id;
    private String desc;
    private String end_time;
    private int goods_id;
    private String name;
    private Ext_infoEntity ext_info;
    private String start_time;
    private int number;
    private int type;
    private int is_finished;

    public void setId(int id) {
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExt_info(Ext_infoEntity ext_info) {
        this.ext_info = ext_info;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setIs_finished(int is_finished) {
        this.is_finished = is_finished;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getEnd_time() {
        return end_time;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public String getName() {
        return name;
    }

    public Ext_infoEntity getExt_info() {
        return ext_info;
    }

    public String getStart_time() {
        return start_time;
    }

    public int getNumber() {
        return number;
    }

    public int getType() {
        return type;
    }

    public int getIs_finished() {
        return is_finished;
    }

    public class Ext_infoEntity {
        /**
         * deposit : 0
         * price_ladder : [{"amount":1,"price":100}]
         * buy_amount : 0
         * gift_integral : 0
         * restrict_amount : 98
         */
        private int deposit;
        private List<Price_ladderEntity> price_ladder;
        private int buy_amount;
        private int gift_integral;
        private int restrict_amount;

        public void setDeposit(int deposit) {
            this.deposit = deposit;
        }

        public void setPrice_ladder(List<Price_ladderEntity> price_ladder) {
            this.price_ladder = price_ladder;
        }

        public void setBuy_amount(int buy_amount) {
            this.buy_amount = buy_amount;
        }

        public void setGift_integral(int gift_integral) {
            this.gift_integral = gift_integral;
        }

        public void setRestrict_amount(int restrict_amount) {
            this.restrict_amount = restrict_amount;
        }

        public int getDeposit() {
            return deposit;
        }

        public List<Price_ladderEntity> getPrice_ladder() {
            return price_ladder;
        }

        public int getBuy_amount() {
            return buy_amount;
        }

        public int getGift_integral() {
            return gift_integral;
        }

        public int getRestrict_amount() {
            return restrict_amount;
        }

        public class Price_ladderEntity {
            /**
             * amount : 1
             * price : 100
             */
            private int amount;
            private int price;

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getAmount() {
                return amount;
            }

            public int getPrice() {
                return price;
            }
        }
    }
}
