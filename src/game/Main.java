package game;

import java.util.*;

import static game.InOutUtils.readStringsFromInputStream;
import static game.ProcessUtils.UTF_8;

/**
 * Main samplegame class.
 */
public class Main {

    public static void main(String[] args) {
        List<String> input = readStringsFromInputStream(System.in, UTF_8);
        if(!input.isEmpty()){
            Round round = new Round(input);
            printMovingGroups(makeMove(round));
        }
        System.exit(0);
    }

    private static List<MovingGroup> makeMove(Round round) {
        List<MovingGroup> movingGroups = new ArrayList<>();
        // Место для Вашего кода.

        //узнаём исходную позицию себя и противника (0 или 9).
        int myStart = round.getTeamId();
        if (myStart == 1) {
            myStart = 9;
        };

        //высаживаем десант на ближайшие 3 планеты (количество людей - на 1 больше, чем есть на планете.
        List<DistFromTo> myDistList = new ArrayList<>();
        if (round.getCurrentStep() < 3){
            for (int k = 0; k < 10; k++){
                myDistList.add(new DistFromTo());
                int d = round.getDistanceMap()[0][k];
                myDistList.get(k).setDist(d);
                myDistList.get(k).setIndex(k);
            }

            myDistList.sort(new Comparator<DistFromTo>() {
                @Override
                public int compare(DistFromTo o1, DistFromTo o2) {
                    return o1.getDist() - o2.getDist();
                }
            });

            for (int k = 1; k<=3; k++){
                int curPlanetIndex = myDistList.get(k).getIndex();
                int numPerson = round.getPlanets().get(curPlanetIndex).getPopulation();
                int reproduction = round.getPlanets().get(curPlanetIndex).getReproduction();
                movingGroups.add(new MovingGroup(myStart, curPlanetIndex,numPerson*reproduction));
            }

        } else {
            //свободные планеты.
            List<Planet> freePlanet = round.getNoMansPlanets();

            //планеты противника
            List<Planet> adversPlanet = round.getAdversarysPlanets();

            //список для отправки.
            List<Planet> allPlanet = new ArrayList<>();
            adversPlanet.forEach(el -> allPlanet.add(el));
            freePlanet.forEach(el -> allPlanet.add(el));


            //список всех перемещающихся групп противника.
            List<MovingGroup> adversGroups = new ArrayList<>();
            adversGroups = round.getAdversarysMovingGroups();

            //список всех моих перемещающихся групп.
            List<MovingGroup> myGroups = new ArrayList<>();
            myGroups = round.getOwnMovingGroups();

            //предварительный список на отправку.
            List<CandidatePlanet> candPlanetList = new ArrayList<>();
            //int countWantToGo = 0;

            List<CandidatePlanet> candPlanetListPremium = new ArrayList<>();
            //int countWantToGo = 0;

            List<Planet> myPlanets = round.getOwnPlanets();
            Planet myCurPlanet = myPlanets.get(0);
            Integer myID = new Integer(0);

            for (int x = 0; x < myPlanets.size(); x++) {
                myCurPlanet = myPlanets.get(x);
                myID = myCurPlanet.getId();
                //перебираем все планеты.
                Planet planetToGo = new Planet();
                for (int i = 0; i < allPlanet.size(); i++) {
                    planetToGo = allPlanet.get(i);

                    //ищем вражеский десант, который летит на эту планету.
                    MovingGroup oneAdversGroup = null;

                    for (int j = 0; j < adversGroups.size(); j++) {
                        if (planetToGo.getId() == adversGroups.get(j).getTo()) {
                            oneAdversGroup = adversGroups.get(j);
                        }
                    }

                    //ищем наш десант, который летит на эту планету.
                    MovingGroup oneMyGroup = null;

                    for (int j = 0; j < myGroups.size(); j++) {
                        if (planetToGo.getId() == myGroups.get(j).getTo()) {
                            oneMyGroup = myGroups.get(j);
                        }
                    }

                    //заполняем поля.
                    int adversGroupCount = 0;
                    int myGroupCount = 0;
                    if (oneAdversGroup != null) {
                        adversGroupCount = oneAdversGroup.getCount();
                    }
                    ;
                    if (oneMyGroup != null) {
                        myGroupCount = oneMyGroup.getCount();
                    }
                    ;

                    int ownPopulation = planetToGo.getPopulation();
                    int myDist = round.getDistanceMap()[myID][planetToGo.getId()];


                    //если мы летим на планету.
                    if (myGroupCount != 0) {
                        //и противник летит на планету.
                        if ((adversGroupCount != 0)
                                && (myDist < round.getMaxSteps() - round.getCurrentStep())
                                && (myCurPlanet.getPopulation() > adversGroupCount + ownPopulation)) {

                            CandidatePlanet oneCandidate = new CandidatePlanet();
                            oneCandidate.setPopulation(adversGroupCount + ownPopulation);
                            oneCandidate.setReproduction(planetToGo.getReproduction());
                            oneCandidate.setIndex(planetToGo.getId());
                            oneCandidate.setDist(myDist);
                            oneCandidate.setFrom(myID);

                            candPlanetListPremium.add(oneCandidate);
                            //System.out.println("Добавили в лист ожидания premium.");
                        }
                    } else {
                        //если хватает ходов и людей - пишем в предварительный список.
                        if ((myDist < round.getMaxSteps() - round.getCurrentStep())
                                && (myCurPlanet.getPopulation() > adversGroupCount + ownPopulation)) {
                            CandidatePlanet oneCandidate = new CandidatePlanet();
                            oneCandidate.setPopulation(adversGroupCount + ownPopulation);
                            oneCandidate.setReproduction(planetToGo.getReproduction());
                            oneCandidate.setIndex(planetToGo.getId());
                            oneCandidate.setDist(myDist);
                            oneCandidate.setFrom(myID);

                            candPlanetList.add(oneCandidate);
                            //System.out.println("Добавили в лист ожидания.");
                        }
                    }

                }

            }

            candPlanetList.sort(new Comparator<CandidatePlanet>() {
                @Override
                public int compare(CandidatePlanet o1, CandidatePlanet o2) {
                    return (o1.getSumPopulation() + o1.getDist()) - (o2.getSumPopulation() + o2.getDist());
                }
            });

            candPlanetListPremium.sort(new Comparator<CandidatePlanet>() {
                @Override
                public int compare(CandidatePlanet o1, CandidatePlanet o2) {
                    return (o1.getSumPopulation() + o1.getDist()) - (o2.getSumPopulation() + o2.getDist());
                }
            });

            int sumCountMyGroups = 0;
            int sumCountMyPlanetsMans = 0;

            for (int t = 0; t < myPlanets.size(); t++){
                sumCountMyPlanetsMans = sumCountMyPlanetsMans + myPlanets.get(t).getPopulation();
            };

            int k = 0;
            while ((sumCountMyGroups < sumCountMyPlanetsMans)
                    && (!(candPlanetListPremium.isEmpty()))
                    && (k < candPlanetListPremium.size())) {
                //System.out.println("Список ожидания premium.");

                movingGroups.add(new MovingGroup(candPlanetListPremium.get(k).getFrom(), candPlanetListPremium.get(k).getIndex(), candPlanetListPremium.get(k).getSumPopulation() + candPlanetListPremium.get(k).getReprodaction() * allPlanet.get(candPlanetListPremium.get(k).getIndex()).getPopulation() - candPlanetListPremium.get(k).getCountWasGoByMe() + 1));
                sumCountMyGroups = sumCountMyGroups + candPlanetListPremium.get(k).getSumPopulation() - candPlanetListPremium.get(k).getCountWasGoByMe() + 1 ;
                k++;
            }

            k = 0;
            while ((sumCountMyGroups < round.getMaxSteps() - round.getCurrentStep())
                    && (!(candPlanetList.isEmpty()))
                    && (k < candPlanetList.size())) {
                //System.out.println("Список ожидания.");
//            for (int p = 0; p < candPlanetList.size(); p++) {
//                System.out.println(candPlanetList.get(p));
//            }
                movingGroups.add(new MovingGroup(candPlanetList.get(k).getFrom(), candPlanetList.get(k).getIndex(), candPlanetList.get(k).getSumPopulation() + allPlanet.get(candPlanetList.get(k).getIndex()).getPopulation() + 1));
                sumCountMyGroups = sumCountMyGroups + candPlanetList.get(k).getSumPopulation();
                k++;
            }

            //лог.
//        for (int i = 0; i<10; i++){
//           System.out.println(myDistList[i].dist);
//        }
        }

        return movingGroups;
    }

    private static void printMovingGroups(List<MovingGroup> moves) {
        System.out.println(moves.size());
        moves.forEach(move -> System.out.println(move.getFrom() + " " + move.getTo() + " " + move.getCount()));
    }

}
