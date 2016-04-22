package br.usp.falvojr.sudoku.heuristic.impl;

import java.util.HashMap;

import br.usp.falvojr.sudoku.heuristic.Heuristic;
import br.usp.falvojr.sudoku.util.SuDokus;

/**
 * Forward Checking (FC) heuristic.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class ForwardChecking implements Heuristic {

    private HashMap<String, String> domains;
    public int[] domains2 = new int[82];
    public boolean emptyDomainFlag;

    public HashMap<String, String> getDomains() {
	return this.domains;
    }

    public void setDomains(HashMap<String, String> domains) {
	this.domains = domains;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getClonedDomains() {
	return (HashMap<String, String>) this.domains.clone();
    }

    @Override
    public void init(Integer[][] sudoku) {
	this.domains = new HashMap<>();
	for (int row = 0; row < SuDokus.BOARD_SIZE; row++) {
	    for (int col = 0; col < SuDokus.BOARD_SIZE; col++) {
		final String key = SuDokus.generateKey(row, col);
		final StringBuilder possibleValues = new StringBuilder();
		if (sudoku[row][col] == 0) {
		    for (int value = 1; value <= SuDokus.BOARD_SIZE; value++) {
			if (SuDokus.isLegal(row, col, value, sudoku)) {
			    possibleValues.append(value);
			}
		    }
		}
		this.domains.put(key, possibleValues.toString());
	    }
	}
    }

    public void sync(int row, int col, int value, Integer[][] sudoku) {
	// verify if exists rows synchronizations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    final String key = SuDokus.generateKey(row, i);
	    this.syncKey(key, value);
	}
	// verify if exists columns synchronizations
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    final String key = SuDokus.generateKey(i, col);
	    this.syncKey(key, value);
	}
	// verify if exists boxes synchronizations
	final int boxRowOffset = (row / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	final int boxColOffset = (col / SuDokus.BOX_SIZE) * SuDokus.BOX_SIZE;
	for (int i = 0; i < SuDokus.BOX_SIZE; i++) {
	    for (int j = 0; j < SuDokus.BOX_SIZE; j++) {
		final String key = SuDokus.generateKey(boxRowOffset + i, boxColOffset + j);
		this.syncKey(key, value);
	    }
	}
    }

    public void syncKey(final String key, int value) {
	final String oldPossibilities = this.domains.get(key);
	final String syncValue = String.valueOf(value);
	if (oldPossibilities.contains(syncValue)) {
	    this.domains.put(key, oldPossibilities.replace(syncValue, ""));
	}
    }

    /***********************************************************************************************/

    public void preInit2() {

	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) { 		//row
	    for (int j = 0; j < SuDokus.BOARD_SIZE; j++) { 	// col
		// creates 81 domains with the values 1-9 in them
		final int cell = (i * 9) + 1 + j;
		// bits represent 1 base values eg 987654321
		this.domains2[cell] = 0x1ff;
	    }
	}
    }

    public void init2(Integer[][] sudoku) {
	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {
	    for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {
		if (sudoku[i][j] != 0) {
		    final int cell = (i * 9) + 1 + j;
		    final int value = sudoku[i][j];
		    final int newDomain = (1 << value - 1);
		    this.domains2[cell] = newDomain;
		}
	    }
	}
	fcAllRow(sudoku);
	fcAllCol(sudoku);
	fcAllBox(sudoku);
    }

    /**
     * Forward Checks every row in the puzzle
     */
    private void fcAllRow(Integer[][] sudoku) {

	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {

	    int initialValues = 0;
	    /* Get all the values in the row */
	    for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {
		if (sudoku[i][j] != 0) {
		    initialValues |= (1 << (sudoku[i][j] -1));
		}
	    }

	    /* If there are values that need to be removed from domains remove them */
	    if (initialValues != 0) {
		removeRowDomainValues(i, initialValues, sudoku);
	    }
	    /* if we have created an empty domain stop doing forward checking */
	    if (this.emptyDomainFlag) {
		break;
	    }
	}
    }

    /**
     * Forward Checks every column in the puzzle
     */
    private void fcAllCol(Integer[][] sudoku) {

	for (int i = 0; i < SuDokus.BOARD_SIZE; i++) {

	    int initialValues = 0;
	    /* Get all the values in the column */
	    for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {
		if (sudoku[j][i] != 0) {
		    initialValues |= (1 << (sudoku[i][j] -1));
		}
	    }
	    /* If there are values that need to be removed from domains remove them */
	    if (initialValues != 0) {
		removeColDomainValues(i, initialValues, sudoku);
	    }

	    /* if we have created an empty domain stop doing forward checking */
	    if (this.emptyDomainFlag) {
		break;
	    }
	}
    }

    /**
     * Forward Checks every box in the puzzle
     */
    private void fcAllBox(Integer[][] sudoku) {

	int cellRow, cellCol;

	escape:
	    /**
	     * Loop through all the boxes Finds the top left square of each 3x3 box.
	     */
	    for (int i = 0; i < 3; i++) {
		for (int j = 0; j < 3; j++) {
		    int initialValues = 0;

		    /* loops through each cell in a 3x3 box */
		    for (int x = 0; x < SuDokus.BOX_SIZE; x++) {
			for (int y = 0; y < SuDokus.BOX_SIZE; y++) {
			    cellRow = (3 * i) + x;
			    cellCol = (3 * j) + y;

			    if (sudoku[cellRow][cellCol] != 0) {
				//creates a binary number representing domains
				//| Bitwise inclusive operand adds the value to the initial values int
				initialValues |= (1 << (sudoku[cellRow][cellCol] -1));
			    }
			}
		    }

		    /* If there are values that need to be removed from domains remove them */
		    if (initialValues != 0) {
			removeBoxDomainValues(i*3, j*3, initialValues, sudoku);
		    }

		    /* if we have created an empty domain stop doing forward checking */
		    if (this.emptyDomainFlag) {
			break escape;       //Break all loops
		    }
		}
	    }
    }

    /**
     * Remove the values from all of the domains in the row
     */
    public void removeRowDomainValues(int row, int initialValues, Integer[][] sudoku) {
	for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {

	    /* convert I,J coord into numerical value of 81 to find the domain */
	    final int cell = (row * 9) + 1 + j;

	    this.domains2[cell] &= ~initialValues;

	    if (sudoku[row][j] != 0){
		final int value = sudoku[row][j];
		final int bitValue = (1<<value-1);
		//domains[cell] |= bitValue;        //add to existing bits, but there shouldnt be any so...
		this.domains2[cell] = bitValue;
	    }

	    /* if a domain shrinks to nothing we have an invalid solution, turn back now! */
	    if (this.domains2[cell] < 1) {
		//System.out.println("Empty Domain --->"+cell+" Domain:"+domain);
		this.emptyDomainFlag = true;
		break;
	    }
	}
    }

    /**
     * Remove the values from all of the domains in the column
     */
    public void removeColDomainValues(int col, int initialValues, Integer[][] sudoku) {
	for (int j = 0; j < SuDokus.BOARD_SIZE; j++) {
	    /* convert I,J coord into numerical value of 81 to find the domain */
	    final int cell = (j * 9) + 1 + col;

	    this.domains2[cell] &= ~initialValues;

	    if (sudoku[j][col] != 0){
		final int value = sudoku[j][col];
		final int bitValue = (1<<value-1);
		//domains[cell] |= bitValue;        //add to existing bits, but there shouldnt be any so...
		this.domains2[cell] = bitValue;
	    }

	    /* if a domain shrinks to nothing we have an invalid solution, turn back now! */
	    if (this.domains2[cell] < 1) {
		//System.out.println("Empty Domain --->"+cell+" Domain:"+domain);
		this.emptyDomainFlag = true;
		break;
	    }
	}
    }

    private void removeBoxDomainValues(int boxRow, int boxCol, int initialValues, Integer[][] sudoku) {

	escape:
	    for (int cellRow = boxRow; cellRow < boxRow+SuDokus.BOX_SIZE; cellRow++) {
		for (int cellCol = boxCol; cellCol < boxCol+SuDokus.BOX_SIZE; cellCol++) {

		    /* convert coords into numerical value of 81 to find the domain */
		    final int cell = (cellRow * 9) + 1 + cellCol;

		    this.domains2[cell] &= ~initialValues;

		    if (sudoku[cellRow][cellCol] != 0){
			final int value = sudoku[cellRow][cellCol];
			final int bitValue = (1<<value-1);
			//domains[cell] |= bitValue;        //add to existing bits, but there shouldnt be any so...
			this.domains2[cell] = bitValue;
		    }

		    /* if a domain shrinks to nothing we have an invalid solution, turn back now! */
		    if (this.domains2[cell] < 1) {
			//System.out.println("Empty Domain --->"+cell+" Domain:"+domain);
			this.emptyDomainFlag = true;
			break escape;       //breaks from all loops
		    }
		}
	    }
    }

    /**
     * Empties the relevant domains for the cell just updated.
     *
     * It first creates a backup of the domains and then empties them all while
     * there is no flag set that there is an empty domain.
     *
     * Once done if there is an empty domain it clears the domains and rolls
     * them back again
     */
    public void emptyDomains(int row, int col, Integer[][] sudoku) {
	fcRow(row, col, sudoku);
	if (!this.emptyDomainFlag) {
	    fcCol(row, col, sudoku);
	}
	if (!this.emptyDomainFlag) {
	    fcBox(row, col, sudoku);
	}
    }


    /**
     * Forward check just 1 row, determined by the @param currentCell.
     */
    private void fcRow(int row, int col, Integer[][] sudoku) {
	int initialValues = 0;
	final int currentValue = sudoku[row][col];
	if (sudoku[row][col] != 0) {
	    initialValues = (1<<(currentValue-1));
	}

	/* If there are values that need to be removed from domains remove them */
	if (initialValues != 0) {
	    removeRowDomainValues(row, initialValues, sudoku);
	}
    }

    /**
     * Forward check just 1 column, determined by the @param currentCell.
     */
    private void fcCol(int row, int col, Integer[][] sudoku) {
	int initialValues = 0;
	final int currentValue = sudoku[row][col];
	if (sudoku[row][col] != 0) {
	    initialValues = (1<<(currentValue-1));
	}

	/* If there are values that need to be removed from domains remove them */
	if (initialValues != 0) {
	    removeColDomainValues(col, initialValues, sudoku);
	}
    }

    private void fcBox(int row, int col, Integer[][] sudoku) {
	int initialValues = 0;
	final int currentValue = sudoku[row][col];

	/* Finds the top left square of the current 3x3 box. */
	final int boxRow = row - (row % 3);
	final int boxCol = row - (row % 3);


	if (sudoku[row][col] != 0) {
	    initialValues = (1<<(currentValue-1));
	}

	/* If there are values that need to be removed from domains remove them */
	if (initialValues != 0) {
	    removeBoxDomainValues(boxRow, boxCol, initialValues, sudoku);
	}

    }

    /**
     * Private constructor for Singleton Pattern.
     */
    private ForwardChecking() {
	super();
    }

    private static class ForwardCheckingHolder {
	public static final ForwardChecking INSTANCE = new ForwardChecking();
    }

    public static ForwardChecking getInstance() {
	return ForwardCheckingHolder.INSTANCE;
    }

}
