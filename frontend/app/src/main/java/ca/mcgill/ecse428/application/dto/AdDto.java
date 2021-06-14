package ca.mcgill.ecse428.application.dto;

import java.util.Date;

public class AdDto {
    private String id;
    private String address;
    private Date pickupStart;
    private Date pickupEnd;
    private Date dropoffStart;
    private Date dropoffEnd;
    private double weight;
    private String zipcode;
    private String clothingDesc;
    private String specialInst;
    private boolean bleach;
    private boolean iron;
    private boolean fold;
    private String phoneNum;
    private String account;

    public String getServices(){
        String s="";
        if (bleach) s+="bleach, ";
        if (iron) s+="iron, ";
        if (fold) s+="fold";
        else s="None";
        return s;
    }

    public String getPickupWindow(){
        return formatDateWindow(pickupStart,pickupEnd);
    }

    public String getDropoffWindow(){
        return formatDateWindow(dropoffStart,dropoffEnd);
    }

    private String formatDateWindow(Date start,Date end){
        String [] startSplits = start.toString().split(" ");
        String [] endSplits=end.toString().split(" ");

        String month=startSplits[1];
        String day = startSplits[2];
        String startTime=startSplits[3].substring(0,5);
        String endTime=endSplits[3].substring(0,5);

        String s = startTime+"-"+endTime+" | "+month+" "+day;

        return s;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getPickupStart() {
        return pickupStart;
    }

    public void setPickupStart(Date pickupStart) {
        this.pickupStart = pickupStart;
    }

    public Date getPickupEnd() {
        return pickupEnd;
    }

    public void setPickupEnd(Date pickupEnd) {
        this.pickupEnd = pickupEnd;
    }

    public Date getDropoffStart() {
        return dropoffStart;
    }

    public void setDropoffStart(Date dropoffStart) {
        this.dropoffStart = dropoffStart;
    }

    public Date getDropoffEnd() {
        return dropoffEnd;
    }

    public void setDropoffEnd(Date dropoffEnd) {
        this.dropoffEnd = dropoffEnd;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getClothingDesc() {
        return clothingDesc;
    }

    public void setClothingDesc(String clothingDesc) {
        this.clothingDesc = clothingDesc;
    }

    public String getSpecialInst() {
        return specialInst;
    }

    public void setSpecialInst(String specialInst) {
        this.specialInst = specialInst;
    }

    public boolean isBleach() {
        return bleach;
    }

    public void setBleach(boolean bleach) {
        this.bleach = bleach;
    }

    public boolean isIron() {
        return iron;
    }

    public void setIron(boolean iron) {
        this.iron = iron;
    }

    public boolean isFold() {
        return fold;
    }

    public void setFold(boolean fold) {
        this.fold = fold;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "AdDto{" +"\n"+
                "id=" + id + "\n"+
                ", address='" + address + "\n" +
                ", pickupStart=" + pickupStart + "\n" +
                ", pickupEnd=" + pickupEnd + "\n" +
                ", dropoffStart=" + dropoffStart + "\n" +
                ", dropoffEnd=" + dropoffEnd + "\n" +
                ", weight=" + weight + "\n" +
                ", zipcode='" + zipcode + "\n" +
                ", clothingDesc='" + clothingDesc + "\n" +
                ", specialInst='" + specialInst + "\n" +
                ", bleach=" + bleach + "\n" +
                ", iron=" + iron + "\n" +
                ", fold=" + fold + "\n" +
                ", phoneNum='" + phoneNum + "\n" +
                ", creator='" + account + "\n" +
                '}';
    }
}
