package com.maimai.tamagotchi.item.impl;

import com.maimai.tamagotchi.item.DefaultType;

public enum ToyType implements DefaultType {
    WHEEL("Wheel", 50, 25), //Hamster
    LABYRINTH("Labyrinth", 40, 25), //Hamster
    RUBBER_BONE("Rubber_bone", 40, 25), //Dog
    BALL("Ball", 50, 25), //Dog
    PLATFORMS("Platforms", 50, 25), //Parrot
    BELL("Bell", 40, 25), //Parrot
    TUNNEL("Tunnel", 50, 25), //Rabbit
    LADDER("Ladder", 40, 25), //Rabbit
    ROPE("Rope", 50, 25), //Cat
    POINTER("Pointer", 40, 25); //Cat

    private final String name;
    private final double value;
    private final double cost;

    ToyType(String name, double value, double cost) {
        this.name = name;
        this.value = value;
        this.cost = cost;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public double getCost() {
        return cost;
    }
}
