package com.rug.gea.Model;

import java.io.Serializable;

public class Information implements Serializable {
    public enum Request {
        Create, Update, Delete
    }

    private Request request;
    private Client client;

    public Information(Request request, Client client) {
        this.request = request;
        this.client = client;
    }

    public Request getRequest() {
        return request;
    }

    public Client getClient() {
        return client;
    }
}
