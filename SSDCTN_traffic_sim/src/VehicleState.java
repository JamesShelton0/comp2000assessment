public interface VehicleState {
    void detectStraight();
    void detectTurn();
    void move(Turn turn);
}
