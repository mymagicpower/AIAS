package top.aias.dl4j.examples.detection.dataHelpers;

import java.util.LinkedList;
import java.util.List;

public class ConsoleTable{

    /*
     * Modify these to suit your use
     */
    private final int TABLEPADDING = 4;
    private final char SEPERATOR_CHAR = '-';

    private List<String> headers;
    private LinkedList<LinkedList<String>> table;
    private List<Integer> maxLength;
    private int rows,cols;

    /*
     * Constructor that needs two arraylist
     * 1: The headersIs is one list containing strings with the columns headers
     * 2: The content is an matrix of Strings build up with ArrayList containing the content
     *
     * Call the printTable method to print the table
     */

    public ConsoleTable(List<String> headersIn, LinkedList<LinkedList<String>> content){
        this.headers = headersIn;
        this.maxLength =  new LinkedList<Integer>();
        //Set headers length to maxLength at first
        for (String header : headers) {
            maxLength.add(header.length());
        }
        this.table = content;
        calcMaxLengthAll();
    }
    /*
     * To update the matrix
     */
    public void updateField(int row, int col, String input){
        //Update the value
        table.get(row).set(col,input);
        //Then calculate the max length of the column
        calcMaxLengthCol(col);
    }
    /*
     * Prints the content in table to console
     */
    @Override
    public String toString(){
        //Take out the
        StringBuilder sb = new StringBuilder("\n");
        StringBuilder sbRowSep = new StringBuilder();
        StringBuilder paddler = new StringBuilder();
        String rowSeperator;

        //Create padding string containing just containing spaces
        for(int i = 0; i < TABLEPADDING; i++){
            paddler.append(" ");
        }

        //Create the rowSeperator
        for (Integer integer : maxLength) {
            sbRowSep.append("|");
            for (int j = 0; j < integer + (TABLEPADDING * 2); j++)
                sbRowSep.append(SEPERATOR_CHAR);
        }
        sbRowSep.append("|");
        rowSeperator = sbRowSep.toString();

        sb.append(rowSeperator);
        sb.append("\n");
        //HEADERS
        sb.append("|");
        for(int i = 0; i < headers.size(); i++){
            sb.append(paddler);
            sb.append(headers.get(i));
            //Fill up with empty spaces
            for(int k = 0; k < (maxLength.get(i)-headers.get(i).length()); k++){
                sb.append(" ");
            }
            sb.append(paddler);
            sb.append("|");
        }
        sb.append("\n");
        sb.append(rowSeperator);
        sb.append("\n");

        //BODY
        for (List<String> tempRow : table) {
            //New row
            sb.append("|");
            for (int j = 0; j < tempRow.size(); j++) {
                sb.append(paddler);
                sb.append(tempRow.get(j));
                //Fill up with empty spaces
                for (int k = 0; k < (maxLength.get(j) - tempRow.get(j).length()); k++) {
                    sb.append(" ");
                }
                sb.append(paddler);
                sb.append("|");
            }
            sb.append("\n");
            sb.append(rowSeperator);
            sb.append("\n");
        }
        return sb.toString();
    }
    /*
     * Fills maxLenth with the length of the longest word
     * in each column
     *
     * This will only be used if the user dont send any data
     * in first init
     */
    private void calcMaxLengthAll(){
        for (List<String> temp : table) {
            for (int j = 0; j < temp.size(); j++) {
                //If the table content was longer then current maxLength - update it
                if (temp.get(j).length() > maxLength.get(j))
                    maxLength.set(j, temp.get(j).length());
            }
        }
    }
    /*
     * Same as calcMaxLength but instead its only for the column given as inparam
     */
    private void calcMaxLengthCol(int col){
        for (LinkedList<String> strings : table)
            if (strings.get(col).length() > maxLength.get(col))
                maxLength.set(col, strings.get(col).length());
    }
}