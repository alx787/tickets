package ru.ath.asu.tickets.rest;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketsRestModel {

    @XmlElement(name = "value")
    private String message;

    public TicketsRestModel() {
    }

    public TicketsRestModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}