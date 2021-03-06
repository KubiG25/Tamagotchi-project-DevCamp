package com.maimai.tamagotchi.tamagotchi;

import com.maimai.tamagotchi.ProgramCore;
import com.maimai.tamagotchi.action.Action;
import com.maimai.tamagotchi.action.SimpleAction;
import com.maimai.tamagotchi.event.tamagotchi.TamagotchiStatsChangeEvent;
import com.maimai.tamagotchi.item.impl.FoodType;
import com.maimai.tamagotchi.item.impl.ToyType;
import com.maimai.tamagotchi.manager.Manager;
import com.maimai.tamagotchi.manager.ManagerImpl;
import com.maimai.tamagotchi.statistic.Statistic;
import com.maimai.tamagotchi.statistic.impl.DoubleStatistic;
import com.maimai.tamagotchi.utils.MessageUtils;

import java.beans.ConstructorProperties;

public abstract class AbstractTamagotchi implements Tamagotchi {

    private final String name;
    private final TamagotchiType type;
    private boolean alive;

    private final Statistic<Double> hunger;
    private final Statistic<Double> health;
    private final Statistic<Double> thirst;
    private final Statistic<Double> dirty;
    private final Statistic<Double> happiness;

    private final Manager<String, Action> actionManager;
    private final Statistic<Double> fatigue;

    @ConstructorProperties({
            "name",
            "type"
    })
    public AbstractTamagotchi(ProgramCore core,
                              String name,
                              TamagotchiType type) {

        this.name = name;
        this.type = type;
        this.alive = true;

        this.hunger = new DoubleStatistic(100);
        this.health = new DoubleStatistic(100);
        this.thirst = new DoubleStatistic(100);
        this.dirty = new DoubleStatistic(0);
        this.happiness = new DoubleStatistic(100);
        this.fatigue = new DoubleStatistic(0);

        this.actionManager = new ManagerImpl<>();

        registerDefaultActions(core);
        registerActions(core);

    }

    protected abstract void registerActions(ProgramCore core);

    protected void registerAction(String name, Action action) {
        getActionManager().insert(name, action);
    }

    @Override
    public void registerDefaultActions(ProgramCore core) {
        registerAction("play", new SimpleAction.Builder()
                .createRequirement((player, item) -> {
                    if(item == null) {
                        MessageUtils.sendMessageFromLang(core, "actions.requiresItem");
                        return false;
                    }
                    if(!(player.getTamagotchi().getFatigue().getValue() >= 70)){
                        switch (player.getTamagotchi().getType()){
                            case CAT:
                                if (item.getDefaultType() == ToyType.ROPE || item.getDefaultType() == ToyType.POINTER){
                                    return true;
                                }
                                break;
                            case DOG:
                                if (item.getDefaultType() == ToyType.RUBBER_BONE || item.getDefaultType() == ToyType.BALL){
                                    return true;
                                }
                                break;
                            case PARROT:
                                if (item.getDefaultType() == ToyType.PLATFORMS || item.getDefaultType() == ToyType.BELL){
                                    return true;
                                }
                                break;
                            case HAMSTER:
                                if (item.getDefaultType() == ToyType.WHEEL || item.getDefaultType() == ToyType.LABYRINTH){
                                    return true;
                                }
                                break;
                            case RABBIT:
                                if (item.getDefaultType() == ToyType.TUNNEL || item.getDefaultType() == ToyType.LADDER){
                                    return true;
                                }
                                break;
                        }
                        MessageUtils.sendMessageFromLang(core, "tamagotchi.itemCanNotBeUsed");
                    }else{
                        MessageUtils.sendMessageFromLang(core, "tamagotchi.canNotPlay", player.getTamagotchi().getName());
                    }
                    return false;
                })
                .createExecutor((player, item)-> {
                    core.getEventRegister().callEvent(new TamagotchiStatsChangeEvent(player.getTamagotchi()));
                    player.getTamagotchi().getFatigue().increase(30D);
                    player.getTamagotchi().getHunger().decrement(30D);
                    player.getTamagotchi().getThirst().decrement(30D);
                    player.getTamagotchi().getDirty().increase(30D);
                    player.getMoney().increase(10D);
                    switch (player.getTamagotchi().getType()){
                        case CAT:
                            System.out.println("Miauuu");
                            break;
                        case DOG:
                            System.out.println("Guauu guauuu");
                            break;
                        case PARROT:
                            System.out.println("Aagrrrr Aaggrrrrr");
                            break;
                        case HAMSTER:
                            System.out.println("Squeaky squeeaky");
                            break;
                        case RABBIT:
                            System.out.println("Sniff snifff");
                            break;
                    }
                }).build());

        registerAction("feed", new SimpleAction.Builder()
                .createRequirement((player, item) -> {
                    if(item == null) {
                        MessageUtils.sendMessageFromLang(core, "actions.requiresItem");
                        return false;
                    }
                    switch (player.getTamagotchi().getType()) {
                        case CAT:
                        case DOG:
                            if (item.getDefaultType() == FoodType.BEEF || item.getDefaultType() == FoodType.FISH ||
                                    item.getDefaultType() == FoodType.CHICKEN) {
                                return true;
                            }else{
                                MessageUtils.sendMessageFromLang(core, "tamagotchi.canNotEat", player.getTamagotchi().getName());
                            }
                            break;
                        case PARROT:
                        case HAMSTER:
                            if (item.getDefaultType() == FoodType.SEED || item.getDefaultType() == FoodType.APPLE ||
                                    item.getDefaultType() == FoodType.BERRIE || item.getDefaultType() == FoodType.MANGO ||
                                    item.getDefaultType() == FoodType.CARROT || item.getDefaultType() == FoodType.LETTUCE) {
                                return true;
                            }else{
                                MessageUtils.sendMessageFromLang(core, "tamagotchi.canNotEat", player.getTamagotchi().getName());
                            }
                            break;
                        case RABBIT:
                            if (item.getDefaultType() == FoodType.CARROT || item.getDefaultType() == FoodType.LETTUCE) {
                                return true;
                            }else{
                                MessageUtils.sendMessageFromLang(core, "tamagotchi.canNotEat", player.getTamagotchi().getName());
                            }
                            break;
                    }
                    return false;
                })
                .createExecutor((player, item)-> {
                    core.getEventRegister().callEvent(new TamagotchiStatsChangeEvent(player.getTamagotchi()));
                    player.getMoney().increase(5D);
                    MessageUtils.sendMessageFromLang(core, "tamagotchi.canEat", player.getTamagotchi().getName());
                }).build());

        registerAction("sleep", new SimpleAction.Builder()
                .createRequirement((player, item) -> {
                    if(item == null) {
                        return true;
                    }
                    MessageUtils.sendMessageFromLang(core, "actions.notRequiresItem");
                    return false;
                })
                .createExecutor((player, item) -> {
                    core.getEventRegister().callEvent(new TamagotchiStatsChangeEvent(player.getTamagotchi()));
                    player.getTamagotchi().getHunger().decrement(40D);
                    player.getTamagotchi().getThirst().decrement(40D);
                    player.getTamagotchi().getHappiness().increase(20D);
                    player.getTamagotchi().getDirty().increase(20D);
                    player.getTamagotchi().getFatigue().decrement(80D);
                    player.getMoney().increase(5D);

                    MessageUtils.sendMessageFromLang(core, "tamagotchi."+ player.getTamagotchi().getType().toString()
                            .toLowerCase()+".sleep", player.getTamagotchi().getName());
                }).build());

        registerAction("water", new SimpleAction.Builder()
                .createRequirement((player, item) -> {
                    if(item == null) {
                        return true;
                    }
                    MessageUtils.sendMessageFromLang(core, "actions.notRequiresItem");
                    return false;
                })
                .createExecutor((player, item) -> {
                    core.getEventRegister().callEvent(new TamagotchiStatsChangeEvent(player.getTamagotchi()));
                    player.getTamagotchi().getThirst().increase(40D);

                    MessageUtils.sendMessageFromLang(core, "tamagotchi.water", player.getTamagotchi().getName());
                }).build());

    }


    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public TamagotchiType getType() {
        return type;
    }

    @Override
    public Manager<String, Action> getActionManager() {
        return actionManager;
    }

    @Override
    public Statistic<Double> getHunger() {
        return hunger;
    }

    @Override
    public boolean isHunger() {
        return hunger.getValue() <= 10;
    }

    @Override
    public Statistic<Double> getHealth() {
        return health;
    }

    @Override
    public Statistic<Double> getThirst() {
        return thirst;
    }

    @Override
    public Statistic<Double> getDirty() {
        return dirty;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Statistic<Double> getHappiness() {
        return happiness;
    }

    @Override
    public Statistic<Double> getFatigue() {
        return fatigue;
    }
}
