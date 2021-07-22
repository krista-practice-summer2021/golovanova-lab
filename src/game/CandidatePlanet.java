package game;

public class CandidatePlanet {
    private int sumPopulation;
    private int reprodaction;
    private int dist;
    private int index;
    private int countWasGoByMe;
    private int countWasGoByAdvers;
    private int from;
    //private Planet planet;


    public void setFrom(int from) {
        this.from = from;
    }

    public int getFrom() {
        return from;
    }

    public void setPopulation(int population){
        this.sumPopulation = population;
    }
    public int getSumPopulation(){
        return (this.sumPopulation);
    }
    public void setReproduction(int reprodaction){
        this.reprodaction = reprodaction;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public int getIndex() {
        return this.index;
    }
    public void setCountWasGoByMe(int count){
        this.countWasGoByMe = count;
    }
    public void setCountWasGoByAdvers(int count){
        this.countWasGoByMe = count;
    }

    public int getCountWasGoByMe() {
        return countWasGoByMe;
    }

    public int getDist() {
        return dist;
    }

    public int getReprodaction() {
        return reprodaction;
    }
}




