package game;

import java.util.List;

public class Player {
    private List<Robot> robots;
    private int activeRobotIndex = 0; // ربات فعال

    public Player(List<Robot> robots) {
        this.robots = robots;
        if (!robots.isEmpty()) {
            activeRobotIndex = 0; // ربات اول به‌صورت پیش‌فرض فعاله
        }
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public Robot getActiveRobot() {
        if (activeRobotIndex >= 0 && activeRobotIndex < robots.size()) {
            return robots.get(activeRobotIndex);
        }
        return null; // اگه لیست خالی باشه یا ایندکس نامعتبر باشه
    }

    public void setActiveRobot(int index) {
        if (index >= 0 && index < robots.size()) {
            activeRobotIndex = index;
            System.out.println("ربات شماره " + (index + 1) + " فعال شد!");
        } else {
            System.out.println("ایندکس نامعتبر است!");
        }
    }
}