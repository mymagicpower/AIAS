package me.aias.common.utils;

import java.util.ArrayList;
import java.util.List;

public class KmersUtils {
    public static void main(String[] args) {
        int k = 4;
        String dna = "ATGGAAAATGGCTGCCTGCTTAACTATCTCAGGGAGAATAAAGGAAAGCTTAGGAAGGAAATGCTACTGAGTGTATGCCAGGATATATGTGAAGGAATGGAATATCTGGAGAGGAATGGCTATATTCATAGGGATTTGGCGGCAAGGAATTGTTTGGTCAGTTCAACATGCATAGTAAAAATTTCAGACTTTGGAATGACAAGGAGTTTTAATGTGGGAAGTTTTTACAGAAGGAAAAATGCCTTTTGA";
        System.out.println("DNA: " + dna);

        System.out.println(buildKmers(dna, k));
    }


    /**
     * Function to get k-mers for sequence s
     *
     * @param sequence
     * @param k
     * @return
     */
    public static List<String> buildKmers(String sequence, int k) {
        //
        List<String> kmers = new ArrayList<>();
        int n = sequence.length() - k + 1;

        for (int i = 0; i < n; i++) {
            String kmer = sequence.substring(i, i + k).toUpperCase();
            kmers.add(kmer);
        }
        return kmers;
    }
}