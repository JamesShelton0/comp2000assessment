public class Road extends Path { 
    // only accepts vehicles in its bounds
    int speedLimit;
    int laneCount;
    int laneMarkings;

    Road(int width, int height) {
        super.width = width;
        super.height = height;
    }

}
