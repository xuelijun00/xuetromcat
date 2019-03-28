package com.yks.bigdata.dto.trackmore;

public class Physical {
    private Integer id;

    private String platform;

    private String country;

    private String channel;

    private String paymentOrder;

    private Integer totalShipment;

    private Float orderExecutionRatio;

    private Integer internetAccess;

    private Float internetRate;

    private Float avgInternetDays;

    private Integer numberOfSeals;

    private Float sealingRate;

    private Float avgNumberDaysIssued;

    private Integer intersectionNumber;

    private Float interchangeRate;

    private Float avgSailingDays;

    private Integer landingNumber;

    private Float landingRate;

    private Float avgLandingDays;

    private Integer tuotouNumber;

    private Float tuotouRate;

    private Float avgNumberDaysTuotou;

    private Float totalWeight;

    private Float avgWeight;

    private Float totalFreight;

    private Float avgUnitPrice;

    private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public String getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(String paymentOrder) {
        this.paymentOrder = paymentOrder == null ? null : paymentOrder.trim();
    }

    public Integer getTotalShipment() {
        return totalShipment;
    }

    public void setTotalShipment(Integer totalShipment) {
        this.totalShipment = totalShipment;
    }

    public Float getOrderExecutionRatio() {
        return orderExecutionRatio;
    }

    public void setOrderExecutionRatio(Float orderExecutionRatio) {
        this.orderExecutionRatio = orderExecutionRatio;
    }

    public Integer getInternetAccess() {
        return internetAccess;
    }

    public void setInternetAccess(Integer internetAccess) {
        this.internetAccess = internetAccess;
    }

    public Float getInternetRate() {
        return internetRate;
    }

    public void setInternetRate(Float internetRate) {
        this.internetRate = internetRate;
    }

    public Float getAvgInternetDays() {
        return avgInternetDays;
    }

    public void setAvgInternetDays(Float avgInternetDays) {
        this.avgInternetDays = avgInternetDays;
    }

    public Integer getNumberOfSeals() {
        return numberOfSeals;
    }

    public void setNumberOfSeals(Integer numberOfSeals) {
        this.numberOfSeals = numberOfSeals;
    }

    public Float getSealingRate() {
        return sealingRate;
    }

    public void setSealingRate(Float sealingRate) {
        this.sealingRate = sealingRate;
    }

    public Float getAvgNumberDaysIssued() {
        return avgNumberDaysIssued;
    }

    public void setAvgNumberDaysIssued(Float avgNumberDaysIssued) {
        this.avgNumberDaysIssued = avgNumberDaysIssued;
    }

    public Integer getIntersectionNumber() {
        return intersectionNumber;
    }

    public void setIntersectionNumber(Integer intersectionNumber) {
        this.intersectionNumber = intersectionNumber;
    }

    public Float getInterchangeRate() {
        return interchangeRate;
    }

    public void setInterchangeRate(Float interchangeRate) {
        this.interchangeRate = interchangeRate;
    }

    public Float getAvgSailingDays() {
        return avgSailingDays;
    }

    public void setAvgSailingDays(Float avgSailingDays) {
        this.avgSailingDays = avgSailingDays;
    }

    public Integer getLandingNumber() {
        return landingNumber;
    }

    public void setLandingNumber(Integer landingNumber) {
        this.landingNumber = landingNumber;
    }

    public Float getLandingRate() {
        return landingRate;
    }

    public void setLandingRate(Float landingRate) {
        this.landingRate = landingRate;
    }

    public Float getAvgLandingDays() {
        return avgLandingDays;
    }

    public void setAvgLandingDays(Float avgLandingDays) {
        this.avgLandingDays = avgLandingDays;
    }

    public Integer getTuotouNumber() {
        return tuotouNumber;
    }

    public void setTuotouNumber(Integer tuotouNumber) {
        this.tuotouNumber = tuotouNumber;
    }

    public Float getTuotouRate() {
        return tuotouRate;
    }

    public void setTuotouRate(Float tuotouRate) {
        this.tuotouRate = tuotouRate;
    }

    public Float getAvgNumberDaysTuotou() {
        return avgNumberDaysTuotou;
    }

    public void setAvgNumberDaysTuotou(Float avgNumberDaysTuotou) {
        this.avgNumberDaysTuotou = avgNumberDaysTuotou;
    }

    public Float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Float getAvgWeight() {
        return avgWeight;
    }

    public void setAvgWeight(Float avgWeight) {
        this.avgWeight = avgWeight;
    }

    public Float getTotalFreight() {
        return totalFreight;
    }

    public void setTotalFreight(Float totalFreight) {
        this.totalFreight = totalFreight;
    }

    public Float getAvgUnitPrice() {
        return avgUnitPrice;
    }

    public void setAvgUnitPrice(Float avgUnitPrice) {
        this.avgUnitPrice = avgUnitPrice;
    }
}