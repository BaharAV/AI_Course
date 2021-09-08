import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static java.util.Collections.sort;

class adad {
    double offset;

    double numbera;
    double numberb;
    double numberc;

    String binaryya;
    String binaryyb;
    String binaryyc;

    adad(double numbera, double numberb, double numberc) {
        this.numbera = numbera;
        this.numberb = numberb;
        this.numberc = numberc;
    }
}

class sorter implements Comparator<adad> {
    public int compare(adad a, adad b) {
        return Double.compare(a.offset, b.offset);
    }
}

public class genetic {

    ArrayList<Double> xs = new ArrayList<Double>();
    ArrayList<Double> ys = new ArrayList<Double>();
    ArrayList<adad> adads = new ArrayList<adad>();

    ArrayList<Double> filler(String place) {
        ArrayList<Double> ss = new ArrayList<Double>();
        String csvFile = place;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] train = line.split(cvsSplitBy);
                ss.add(Double.valueOf(train[0]));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ss;
    }

    ArrayList<adad> randommaker() {
        ArrayList<adad> adads = new ArrayList<adad>();
        Random rd = new Random();
        for (int i = 0; i < 100; i++) {
            double upper = 50.00;
            double lower = -50.00;
            adads.add(new adad(Math.random() * (upper - lower) + lower, Math.random() * (upper - lower) + lower, Math.random() * (upper - lower) + lower));
            adads.get(i).offset = offsetcalculator(adads.get(i));
            adads.get(i).binaryya = numbertobinary(adads.get(i).numbera);
            adads.get(i).binaryyb = numbertobinary(adads.get(i).numberb);
            adads.get(i).binaryyc = numbertobinary(adads.get(i).numberc);
        }
        sort(adads, new sorter());
        for (int i = 99; i >= 30; i--) {
            adads.remove(i);
        }
        return adads;
    }

    void mutation() {
        Random rd = new Random();
        int length = adads.size();
        for (int i = 0; i < length * 0.2; i++) {
            int num = rd.nextInt(length);
            int in = rd.nextInt(60) + 1;
            adads.get(num).binaryya = adads.get(num).binaryya.substring(0, in) + String.valueOf(Math.abs(Integer.parseInt(String.valueOf(adads.get(num).binaryya.charAt(in))) - 1)) + adads.get(num).binaryya.substring(in + 1);
            adads.get(num).binaryyb = adads.get(num).binaryyb.substring(0, in) + String.valueOf(Math.abs(Integer.parseInt(String.valueOf(adads.get(num).binaryyb.charAt(in))) - 1)) + adads.get(num).binaryyb.substring(in + 1);
            adads.get(num).binaryyc = adads.get(num).binaryyc.substring(0, in) + String.valueOf(Math.abs(Integer.parseInt(String.valueOf(adads.get(num).binaryyc.charAt(in))) - 1)) + adads.get(num).binaryyc.substring(in + 1);
            adads.get(num).numbera = binarytonumber(adads.get(num).binaryya);
            adads.get(num).numberb = binarytonumber(adads.get(num).binaryyb);
            adads.get(num).numberc = binarytonumber(adads.get(num).binaryyc);
            adads.get(num).offset = offsetcalculator(adads.get(num));
        }
    }

    String numbertobinary(double number) {
        return Long.toBinaryString(Double.doubleToRawLongBits(number));
    }

    double binarytonumber(String binary) {
        return Double.longBitsToDouble(new BigInteger(binary, 2).longValue());
    }

    double offsetcalculator(adad child) {
        double offset = 0;
        for (int k = 0; k < xs.size(); k++) {
            offset += Math.abs(ys.get(k) - (Math.pow(xs.get(k), 2) * child.numbera + child.numberb * xs.get(k) + child.numberc));
        }
        offset /= xs.size();
        return offset;
    }

    void crossover() {
        Random rd = new Random();
        int length = adads.size();
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                adad child1 = new adad(adads.get(i).numbera, adads.get(i).numberb, adads.get(i).numberc);
                adad child2 = new adad(adads.get(j).numbera, adads.get(j).numberb, adads.get(j).numberc);
                child1.binaryya = numbertobinary(child1.numbera);
                child1.binaryyb = numbertobinary(child1.numberb);
                child1.binaryyc = numbertobinary(child1.numberc);
                child2.binaryya = numbertobinary(child2.numbera);
                child2.binaryyb = numbertobinary(child2.numberb);
                child2.binaryyc = numbertobinary(child2.numberc);
                for (int k = 0; k < 60; k++) {
                    int docrossover = rd.nextInt(2);
                    if (docrossover == 1) {
                        char savea = child1.binaryya.charAt(k);
                        char saveb = child1.binaryyb.charAt(k);
                        char savec = child1.binaryyc.charAt(k);
                        child1.binaryya = child1.binaryya.substring(0, k) + child2.binaryya.charAt(k) + child1.binaryya.substring(k + 1);
                        child2.binaryya = child2.binaryya.substring(0, k) + savea + child2.binaryya.substring(k + 1);
                        child1.binaryyb = child1.binaryyb.substring(0, k) + child2.binaryyb.charAt(k) + child1.binaryyb.substring(k + 1);
                        child2.binaryyb = child2.binaryyb.substring(0, k) + saveb + child2.binaryyb.substring(k + 1);
                        child1.binaryyc = child1.binaryyc.substring(0, k) + child2.binaryyc.charAt(k) + child1.binaryyc.substring(k + 1);
                        child2.binaryyc = child2.binaryyc.substring(0, k) + savec + child2.binaryyc.substring(k + 1);
                    }
                }
                child1.numbera = binarytonumber(child1.binaryya);
                child1.numberb = binarytonumber(child1.binaryyb);
                child1.numberc = binarytonumber(child1.binaryyc);
                child2.numbera = binarytonumber(child2.binaryya);
                child2.numberb = binarytonumber(child2.binaryyb);
                child2.numberc = binarytonumber(child2.binaryyc);
                child1.offset = offsetcalculator(child1);
                child2.offset = offsetcalculator(child2);
                adads.add(child1);
                adads.add(child2);
            }
        }
    }

    void select() {
        Random rd = new Random();
        ArrayList<Integer> letbe = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            letbe.add(rd.nextInt(adads.size()) + 30);
        }
        for (int i = adads.size() - 1; i >= 30; i--) {
            if (!letbe.contains(i)) {
                adads.remove(i);
            }
        }
    }

    void printbests() {
//        double averageoffset = 0;
//        for (int i = 0; i < adads.size(); i++) {
//            averageoffset += adads.get(i).offset;
//        }
//        averageoffset /= adads.size();
        System.out.println("best a " + adads.get(0).numbera);
        System.out.println("best b " + adads.get(0).numberb);
        System.out.println("best c " + adads.get(0).numberc);
        System.out.println("best offset " + adads.get(0).offset);
//        System.out.println("average offset " + averageoffset);
        System.out.println("---");
    }

    void process() {
        int x = 0;
        while (x++ < 20) {
            mutation();
            crossover();
            sort(adads, new sorter());
            select();
            System.out.println("generation " + x);
            printbests();
        }
        System.out.println("result:");
        printbests();
        System.out.println("done!");
    }


    public static void main(String[] args) {

        genetic genetic = new genetic();
        genetic.xs = genetic.filler("../x_train.csv");
        genetic.ys = genetic.filler("../y_train.csv");
        genetic.adads = genetic.randommaker();
        genetic.process();
    }
}
