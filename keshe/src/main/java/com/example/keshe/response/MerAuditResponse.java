package com.example.keshe.response;

import java.util.List;

public class MerAuditResponse {
     private int code;
      private String message;

      private List<Audit> data;
       public static class Audit {
           private Long id;
            private String shop_name;
            private String legal_representative;
            private String id_number;
            private String contact_number;
             private String business_license;
              private String shop_category;
               private String shop_description;
                private String id_card_front;
                 private String id_card_back;
                  private String apply_time;
                   private String audit_status;
                    private String audit_reason;
                     private String shop_address;
                     public Long getId() {
               return id;
           }
           public  void setId(Long id) {
               this.id = id;
           }

           public String getShop_name() {
               return shop_name;
           }

           public void setShop_name(String shop_name) {
               this.shop_name = shop_name;
           }

           public String getLegal_representative() {
               return legal_representative;
           }

           public void setLegal_representative(String legal_representative) {
               this.legal_representative = legal_representative;
           }

           public String getId_number() {
               return id_number;
           }

           public void setId_number(String id_number) {
               this.id_number = id_number;
           }

           public String getContact_number() {
               return contact_number;
           }

           public void setContact_number(String contact_number) {
               this.contact_number = contact_number;
           }

           public String getBusiness_license() {
               return business_license;
           }

           public void setBusiness_license(String business_license) {
               this.business_license = business_license;
           }

           public String getShop_category() {
               return shop_category;
           }

           public void setShop_category(String shop_category) {
               this.shop_category = shop_category;
           }

           public String getShop_description() {
               return shop_description;
           }

           public void setShop_description(String shop_description) {
               this.shop_description = shop_description;
           }

           public String getId_card_front() {
               return id_card_front;
           }

           public void setId_card_front(String id_card_front) {
               this.id_card_front = id_card_front;
           }

           public String getId_card_back() {
               return id_card_back;
           }

           public void setId_card_back(String id_card_back) {
               this.id_card_back = id_card_back;
           }

           public String getApply_time() {
               return apply_time;
           }

           public void setApply_time(String apply_time) {
               this.apply_time = apply_time;
           }

           public String getAudit_status() {
               return audit_status;
           }

           public void setAudit_status(String audit_status) {
               this.audit_status = audit_status;
           }

           public String getAudit_reason() {
               return audit_reason;
           }

           public void setAudit_reason(String audit_reason) {
               this.audit_reason = audit_reason;
           }

           public String getShop_address() {
               return shop_address;
           }

           public void setShop_address(String shop_address) {
               this.shop_address = shop_address;
           }
       }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Audit> getData() {
        return data;
    }

    public void setData(List<Audit> data) {
        this.data = data;
    }

}
