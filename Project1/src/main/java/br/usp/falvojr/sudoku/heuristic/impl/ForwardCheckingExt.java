package br.usp.falvojr.sudoku.heuristic.impl;

/**
 * Concrete SuDoku algorithm implementation using Backtracking With Forward Checking.
 *
 * @see <a href="http://codereview.stackexchange.com/questions/90761/sudoku-solver-using-forward-checking">Sudoku solver using forward checking</a>
 */
class ForwardCheckingExt {

    private static final String NAME = "Backtracking with Forward Checking";

    private static final int GRIDSIZE_ROW = 9;
    private static final int GRIDSIZE_COLUMN = 9;
    private static final int BOXSIZE = 3;

    private int steps =0;

    private Integer[][] solution = new Integer[GRIDSIZE_ROW][GRIDSIZE_COLUMN];
    private int[] domains = new int[82];

    private boolean emptyDomainFlag;

    public ForwardCheckingExt() {
	super();
    }

    public void initDomains() {

	for (int i = 0; i < GRIDSIZE_ROW; i++) {
	    for (int j = 0; j < GRIDSIZE_COLUMN; j++) {
		//creates 81 domains with the values 1-9 in them
		final int cell = (i * 9) + 1 + j;
		this.domains[cell] = 0x1ff;              // bits represent 1 base values eg 987654321
	    }
	}
    }

    /**
     * Largely inefficient, looks through every cell with data assigned and
     * checks if there are too many assignments of the same value in each row
     * column and box for that specific cell.
     */
    public boolean isSolvable(Integer[][] currentBoard) {

	final Integer[][] board = new Integer[GRIDSIZE_ROW][GRIDSIZE_COLUMN];
	for (int i = 0; i < GRIDSIZE_ROW; i++) {
	    System.arraycopy(currentBoard[i], 0, board[i], 0, GRIDSIZE_COLUMN);
	}

	/* Finds the number of cells that have data on the board */
	final int numberOfCells = getCellCount(board);

	for (int i = 0; i < numberOfCells; i++) {
	    final int[] cell = getNextCell(board);

	    final int[] counts = countUsage(board[cell[0]][cell[1]], cell, board);

	    if (counts[0] + counts[1] + counts[2] == 3) {
		board[cell[0]][cell[1]] = null;
	    } else {
		return false;
	    }
	}

	return true;
    }

    /**
     * Counts the amount of cells with data in them on the board.
     */
    private int getCellCount(Integer[][] board) {
	int count = 0;
	for (int i = 0; i < GRIDSIZE_ROW; i++) {
	    for (int j = 0; j < GRIDSIZE_COLUMN; j++) {
		if (board[i][j] != null) {
		    count++;
		}
	    }
	}
	return count;
    }

    /**
     * Gets the next cell with data in it on the board
     */
    private int[] getNextCell(Integer[][] board) {
	final int[] cell = new int[2];
	all:
	    for (int i = 0; i < GRIDSIZE_ROW; i++) {
		for (int j = 0; j < GRIDSIZE_COLUMN; j++) {
		    if (board[i][j] != null) {
			cell[0] = i;
			cell[1] = j;
			break all;      /* used to break out of both loops when a cell is found */

		    }
		}
	    }
	return cell;
    }

    public Integer[][] getSolution() {

	this.emptyDomainFlag = false;
	initDomains();

	//INITIAL FORWARD CHECK
	initial_ForwardCheck();

	if (solveSudoku()) {
	    return this.solution;
	} else {
	    return new Integer[GRIDSIZE_ROW][GRIDSIZE_COLUMN];
	}

    }

    /**
     * The solve algorithm performs a recursive search through values in the
     * domains and attempts to assign them to a cell starting from the upper
     * left cell and proceeds row by row
     */
    private boolean solveSudoku() {

	/* Finds the next empty cell */
	final int[] nextCell = findNextCell();

	/* checks if the cell is not empty, if so we have finished */
	if (this.solution[nextCell[0]][nextCell[1]] != null) {
	    System.out.println("Steps = "+this.steps);
	    return true;
	}

	/**
	 * Converts the coords of the cell into a number between 1-81
	 * representing the cell then it uses that to get the ArrayList of
	 * domains from a HashMap, key = int of cell (1-81)
	 */
	final int cell = (nextCell[0] * 9) + 1 + nextCell[1];

	//        for (int i=0;i<domains.size();i++){
	//            domainSave.put(i+1, domains.get(i));
	//        }
	//domainSave.putAll(domains);
	final int[] domainSave = this.domains.clone();

	/**
	 * Loops through all available domains in the ArrayList and attempts to
	 * use them calls isSafe to ensure that the value can be used in the row
	 * / column / box without clashing with constraints.
	 *
	 * If it can be used within constraints, assign it and then empty the
	 * relevant domains as long as the domains all have possible values,
	 * call this method to solve the next cell if not remove the value and
	 * start again.
	 *
	 * No remaining options = backtrack
	 */
	int domain = this.domains[cell];
	while (domain != 0) {

	    this.steps++;
	    final int lowestBitIndex = Integer.numberOfTrailingZeros(domain);
	    domain &= ~(1 << lowestBitIndex);

	    this.solution[nextCell[0]][nextCell[1]] = lowestBitIndex+1;

	    emptyDomains(nextCell);

	    if (!this.emptyDomainFlag) {
		if (solveSudoku()) {
		    return true;
		}
	    }
	    this.solution[nextCell[0]][nextCell[1]] = null;
	    this.domains = domainSave.clone();
	    this.emptyDomainFlag = false;

	}
	this.domains = domainSave;
	return false;       // Triggers backtracking
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
    public void emptyDomains(int[] cell) {
	fcRow(cell);
	if (!this.emptyDomainFlag) {
	    fcCol(cell);
	}
	if (!this.emptyDomainFlag) {
	    fcBox(cell);
	}
    }

    /**
     * Finds the next empty cell
     */
    private int[] findNextCell() {
	final int[] cell = new int[2];
	all:
	    for (int i = 0; i < GRIDSIZE_ROW; i++) {
		for (int j = 0; j < GRIDSIZE_COLUMN; j++) {
		    if (this.solution[i][j] == null) {
			cell[0] = i;
			cell[1] = j;
			break all;
		    }
		}
	    }
	return cell;
    }

    /**
     * counts the amount of occurrences of data within rows / columns / boxes
     * and returns the numbers in an array.
     */
    private int[] countUsage(int value, int[] cell, Integer[][] board) {
	int countRow = 0;
	int countCol = 0;
	int countBox = 0;

	for (int i = 0; i < GRIDSIZE_COLUMN; i++) {
	    if (board[cell[0]][i] == null) {
	    } else if (board[cell[0]][i] == value) {
		countRow++;
	    }
	}

	for (int i = 0; i < GRIDSIZE_ROW; i++) {
	    if (board[i][cell[1]] == null) {
	    } else if (board[i][cell[1]] == value) {
		countCol++;
	    }
	}

	/* Figures out the start of the current 3x3 box */
	final int boxRow = cell[0] - (cell[0] % 3);
	final int boxCol = cell[1] - (cell[1] % 3);

	for (int i = 0; i < BOXSIZE; i++) {
	    for (int j = 0; j < BOXSIZE; j++) {
		if (board[i + boxRow][j + boxCol] == null) {
		} else if (board[i + boxRow][j + boxCol] == value) {
		    countBox++;
		}
	    }
	}

	final int[] counts = {countRow, countCol, countBox};
	return counts;
    }

    /**
     * Checks if a value is used in the row already, returns false if it is.
     */
    private boolean usedInRow(int value, int row, Integer[][] board) {
	boolean safe = true;
	for (int i = 0; i < GRIDSIZE_COLUMN; i++) {
	    if (board[row][i] == null) {
	    } else if (board[row][i] == value) {
		safe = false;
		break;
	    }
	}
	return safe;
    }

    /**
     * Checks if a value is used in the column already, returns false if it is.
     */
    private boolean usedInColumn(int value, int column, Integer[][] board) {
	boolean safe = true;
	for (int i = 0; i < GRIDSIZE_ROW; i++) {
	    if (board[i][column] == null) {
	    } else if (board[i][column] == value) {
		safe = false;
		break;
	    }
	}
	return safe;
    }

    /**
     * Checks if a value is used in the box already, returns false if it is.
     */
    private boolean usedInBox(int value, int[] cell, Integer[][] board) {
	boolean safe = true;

	/**
	 * boxRow is the starting row of the current box, determined by the
	 * cells x coord - the mod by 3 boxCol is the starting column of the
	 * current box, determined by the cells y coord - the mod by 3
	 */
	final int boxRow = cell[0] - (cell[0] % 3);
	final int boxCol = cell[1] - (cell[1] % 3);

	//* loop through the box and check if any of the values match the selected one */
	for (int i = 0; i < BOXSIZE; i++) {
	    for (int j = 0; j < BOXSIZE; j++) {
		if (board[i + boxRow][j + boxCol] == null) {
		} else if (board[i + boxRow][j + boxCol] == value) {
		    safe = false;
		    break;
		}
	    }
	}

	return safe;
    }

    /**
     * Calls each of the methods to check if a value is already in either row /
     * column / box and if it is return false.
     */
    @SuppressWarnings("unused")
    private boolean isSafe(int value, int[] cell, Integer[][] board) {

	//Check if a number has already been used in the column or row or box
	return usedInRow(value, cell[0], board) && usedInColumn(value, cell[1], board) && usedInBox(value, cell, board);
    }

    /**
     * Performs the initial forward check once a puzzle has been sent to the
     * solver
     */
    public void initial_ForwardCheck() {
	setupInitialDomains();
	fcAllRow();
	fcAllCol();
	fcAllBox();

    }

    public static String stripChars(String input, String strip) {
	final StringBuilder result = new StringBuilder();
	for (final char c : input.toCharArray()) {
	    if (strip.indexOf(c) == -1) {
		result.append(c);
	    }
	}
	return result.toString();
    }

    /**
     * Finds out if the cell has a value if so remove all possible domain
     * values.
     *
     * Once done it adds the value of the cell back to the domain
     */
    private void setupInitialDomains() {
	for (int i = 0; i < GRIDSIZE_ROW; i++) {            //each row
	    for (int j = 0; j < GRIDSIZE_COLUMN; j++) {     //each column
		if (this.solution[i][j] != null) {
		    final int cell = (i * 9) + 1 + j;

		    final int value = this.solution[i][j];

		    final int newDomain = (1 << value-1);
		    this.domains[cell] = newDomain;
		}
	    }
	}
    }

    /**
     * Remove the values from all of the domains in the row
     */
    public void removeRowDomainValues(int row, int initialValues) {
	for (int j = 0; j < GRIDSIZE_COLUMN; j++) {

	    /* convert I,J coord into numerical value of 81 to find the domain */
	    final int cell = (row * 9) + 1 + j;

	    this.domains[cell] &= ~initialValues;

	    if (this.solution[row][j] != null){
		final int value = this.solution[row][j];
		final int bitValue = (1<<value-1);
		//domains[cell] |= bitValue;        //add to existing bits, but there shouldnt be any so...
		this.domains[cell] = bitValue;
	    }

	    /* if a domain shrinks to nothing we have an invalid solution, turn back now! */
	    if (this.domains[cell] < 1) {
		//System.out.println("Empty Domain --->"+cell+" Domain:"+domain);
		this.emptyDomainFlag = true;
		break;
	    }
	}
    }

    /**
     * Remove the values from all of the domains in the column
     */
    public void removeColDomainValues(int col, int initialValues) {
	for (int j = 0; j < GRIDSIZE_ROW; j++) {
	    /* convert I,J coord into numerical value of 81 to find the domain */
	    final int cell = (j * 9) + 1 + col;

	    this.domains[cell] &= ~initialValues;

	    if (this.solution[j][col] != null){
		final int value = this.solution[j][col];
		final int bitValue = (1<<value-1);
		//domains[cell] |= bitValue;        //add to existing bits, but there shouldnt be any so...
		this.domains[cell] = bitValue;
	    }

	    /* if a domain shrinks to nothing we have an invalid solution, turn back now! */
	    if (this.domains[cell] < 1) {
		//System.out.println("Empty Domain --->"+cell+" Domain:"+domain);
		this.emptyDomainFlag = true;
		break;
	    }
	}
    }

    private void removeBoxDomainValues(int boxRow, int boxCol, int initialValues) {

	escape:

	    for (int cellRow = boxRow; cellRow < boxRow+BOXSIZE; cellRow++) {
		for (int cellCol = boxCol; cellCol < boxCol+BOXSIZE; cellCol++) {

		    /* convert coords into numerical value of 81 to find the domain */
		    final int cell = (cellRow * 9) + 1 + cellCol;

		    this.domains[cell] &= ~initialValues;

		    if (this.solution[cellRow][cellCol] != null){
			final int value = this.solution[cellRow][cellCol];
			final int bitValue = (1<<value-1);
			//domains[cell] |= bitValue;        //add to existing bits, but there shouldnt be any so...
			this.domains[cell] = bitValue;
		    }

		    /* if a domain shrinks to nothing we have an invalid solution, turn back now! */
		    if (this.domains[cell] < 1) {
			//System.out.println("Empty Domain --->"+cell+" Domain:"+domain);
			this.emptyDomainFlag = true;
			break escape;       //breaks from all loops
		    }
		}
	    }
    }

    /**
     * Forward Checks every row in the puzzle
     */
    private void fcAllRow() {

	for (int i = 0; i < GRIDSIZE_ROW; i++) {

	    int initialValues = 0;
	    /* Get all the values in the row */
	    for (int j = 0; j < GRIDSIZE_COLUMN; j++) {
		if (this.solution[i][j] != null) {
		    initialValues |= (1 << (this.solution[i][j] -1));
		}
	    }

	    /* If there are values that need to be removed from domains remove them */
	    if (initialValues != 0) {
		removeRowDomainValues(i, initialValues);
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
    private void fcAllCol() {

	for (int i = 0; i < GRIDSIZE_COLUMN; i++) {

	    int initialValues = 0;
	    /* Get all the values in the column */
	    for (int j = 0; j < GRIDSIZE_ROW; j++) {
		if (this.solution[j][i] != null) {
		    initialValues |= (1 << (this.solution[i][j] -1));
		}
	    }
	    /* If there are values that need to be removed from domains remove them */
	    if (initialValues != 0) {
		removeColDomainValues(i, initialValues);
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
    private void fcAllBox() {

	int cellRow, cellCol;

	escape:
	    /**
	     * Loop through all the boxes Finds the top left square of each 3x3 box.
	     */
	    for (int i = 0; i < 3; i++) {
		for (int j = 0; j < 3; j++) {
		    int initialValues = 0;

		    /* loops through each cell in a 3x3 box */
		    for (int x = 0; x < BOXSIZE; x++) {
			for (int y = 0; y < BOXSIZE; y++) {
			    cellRow = (3 * i) + x;
			    cellCol = (3 * j) + y;

			    if (this.solution[cellRow][cellCol] != null) {
				//creates a binary number representing domains
				//| Bitwise inclusive operand adds the value to the initial values int
				initialValues |= (1 << (this.solution[cellRow][cellCol] -1));
			    }
			}
		    }

		    /* If there are values that need to be removed from domains remove them */
		    if (initialValues != 0) {
			removeBoxDomainValues(i*3, j*3, initialValues);
		    }

		    /* if we have created an empty domain stop doing forward checking */
		    if (this.emptyDomainFlag) {
			break escape;       //Break all loops
		    }
		}
	    }
    }

    /**
     * Forward check just 1 row, determined by the @param currentCell.
     */
    private void fcRow(int[] currentCell) {
	int initialValues = 0;
	final int currentValue = this.solution[currentCell[0]][currentCell[1]];
	if (this.solution[currentCell[0]][currentCell[1]] != null) {
	    initialValues = (1<<(currentValue-1));
	}

	/* If there are values that need to be removed from domains remove them */
	if (initialValues != 0) {
	    removeRowDomainValues(currentCell[0], initialValues);
	}
    }

    /**
     * Forward check just 1 column, determined by the @param currentCell.
     */
    private void fcCol(int[] currentCell) {
	int initialValues = 0;
	final int currentValue = this.solution[currentCell[0]][currentCell[1]];
	if (this.solution[currentCell[0]][currentCell[1]] != null) {
	    initialValues = (1<<(currentValue-1));
	}

	/* If there are values that need to be removed from domains remove them */
	if (initialValues != 0) {
	    removeColDomainValues(currentCell[1], initialValues);
	}
    }

    private void fcBox(int[] currentCell) {
	int initialValues = 0;
	final int currentValue = this.solution[currentCell[0]][currentCell[1]];

	/* Finds the top left square of the current 3x3 box. */
	final int boxRow = currentCell[0] - (currentCell[0] % 3);
	final int boxCol = currentCell[1] - (currentCell[1] % 3);


	if (this.solution[currentCell[0]][currentCell[1]] != null) {
	    initialValues = (1<<(currentValue-1));
	}

	/* If there are values that need to be removed from domains remove them */
	if (initialValues != 0) {
	    removeBoxDomainValues(boxRow, boxCol, initialValues);
	}

    }

    public String getName() {
	return NAME;
    }

    public void setSolution(Integer[][] userInputPuzzle) {
	this.solution = userInputPuzzle;
    }

}
