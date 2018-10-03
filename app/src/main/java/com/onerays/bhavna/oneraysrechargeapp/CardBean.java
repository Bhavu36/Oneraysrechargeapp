package com.onerays.bhavna.oneraysrechargeapp;


public class CardBean {

   private int id;
   private long CardNo;
   private String BankName;
   private int Balance;

    public CardBean(int id, long CardNo, String BankName, int Balance){
        this.id = id;
        this.CardNo = CardNo;
        this.BankName = BankName;
        this.Balance = Balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCardNo() {
        return CardNo;
    }

    public void setCardNo(long cardNo) {
        CardNo = cardNo;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }
}
