package game;

public class DistFromTo {
    private int dist;
    private int index;

    public DistFromTo(){

    }
    public DistFromTo(int dist, int index){
        this.dist = dist;
        this.index = index;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public int getDist() {
        return this.dist;
    }

    public void setIndex(int ind) {
        this.index = ind;
    }

    public int getIndex() {
        return this.index;
    }
}