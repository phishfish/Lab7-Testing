package cpsc2150.banking.models;

/**
 *
 *@invariants
 * MIN_YEARS <= years <= MAX_YEARS
 * homeCost >= 0
 * downPayment >= 0
 *
 *@correspondence
 * self.LOAN_REJECTED_PERCENT = LOAN_REJECTED_PERCENT
 * self.debtToIncomeRatio = debtToIncomeRatio
 * self.homeCost = homeCost
 * self.downPayment = downPayment
 * self.years = years
 *
 */

public class Mortgage extends AbsMortgage implements IMortgage{
	//Initializes variables
	public static final double LOAN_REJECTED_PERCENT = 0.1;
	private double debtToIncomeRatio;
	double payment;
	private double homeCost;
	private double downPayment;
	private int years;
	private ICustomer customer;

	/**
	 * This is the constructor function that initializes all information related to Mortgage for a given person
	 * @param cost is the cost of a home
	 *     down is the down payment
	 * 	   length is the number of years it has been
	 * 	   person is the customer whose information has been passed in
	 *
	 * @pre cost > 0
	 *     down > 0
	 *     years >= 0
	 *     [n is a valid customer and exists]
	 *
	 * @post homeCost = cost
	 *     downPayment = down
	 *     years = length
	 *     customer = person
	 *     debtToIncomeRatio = ((monthlyDebtPayments + payment) /(income / MONTHS_IN_YEAR))
	 *     payment = rate/MONTHS_IN_YEAR * principal/
	 *         (1 - (1 + rate/MONTHS_IN_YEAR)^(-1 * years * MONTHS_IN_YEAR))
	 */
	public Mortgage(double cost, double down, int length, ICustomer person){
		//Initializes info passed in
		homeCost = cost;
		downPayment = down;
		years = length;
		customer = person;

		//Calculates the debt to income ratio
		debtToIncomeRatio =
				((customer.getMonthlyDebtPayments() + getPayment()) /(customer.getIncome() / MONTHS_IN_YEAR));
		//Calculates the payment
		payment =
				getRate()/MONTHS_IN_YEAR * getPrincipal()/
						(1 - Math.pow(1 + getRate()/MONTHS_IN_YEAR, -1 * years * MONTHS_IN_YEAR));
	}

	/**
	 * This function returns true if the customer's loan should be approved and false otherwise
	 *
	 * @return Returns false if the rate is lower than LOAN_REJECTED_PERCENT, downpayment is lower than homecost *
	 *     MIN_PERCENT_DOWN, and if the debtToIncomeRatio is higher than DTOITOOHIGH, returns true otherwise
	 *
	 * @post downPayment = #downPayment
	 *     homecost = #homecost
	 *     debtToIncomeRatio = #debtToIncomeRatio
	 */
	public boolean loanApproved(){
		//Returns false if the rate is too low
		if(getRate() > LOAN_REJECTED_PERCENT){
			return false;
		}

		//Returns false if downPayment is too low
		if(downPayment < homeCost * MIN_PERCENT_DOWN){
			return false;
		}

		//Returns false if the debtToIncomeRatio is too high
		if(debtToIncomeRatio >= DTOITOOHIGH){
			return false;
		}

		return true;
	}

	/**
	 * This function is a getter function that returns payment
	 * @return [Returns monthly payments for the loan]
	 * @post payment = #payment
	 */
	public double getPayment(){
		return payment;
	}

	/**
	 * This function gets the Annual Percentage Rate of the customer
	 * @return [Returns the calculated ARP]
	 * @post BASERATE <= apr
	 *     years = #years
	 *     homeCost = #homeCost
	 *     downPayment = #downPayment
	 */
	public double getRate(){
		//Initializes apr to the base rate
		double apr = BASERATE;
		//If it is below the max amount of years, adds GOODRATEADD to apr, else adds NORMALRATEADD
		if(years < MAX_YEARS)
			apr += GOODRATEADD;
		else
			apr += NORMALRATEADD;

		//Adds GoODRATEADD to apr if the condition is true
		if(homeCost * PREFERRED_PERCENT_DOWN > downPayment)
			apr += GOODRATEADD;

		//Adds an amount to apr depending on the user's credit score
		int creditScore = customer.getCreditScore();
		if(creditScore < BADCREDIT)
			apr += VERYBADRATEADD;
		else if(creditScore < FAIRCREDIT)
			apr += BADRATEADD;
		else if(creditScore < GOODCREDIT)
			apr += NORMALRATEADD;
		else if(creditScore < GREATCREDIT)
			apr += GOODRATEADD;

		return apr;
	}

	/**
	 * This is a getter function that calculates and returns the principal of the user
	 * @return [Returns the principal found by subtracting homeCost and downPayment]
	 * @post homeCost = #homeCost
	 *     downPayment = #downPayment
	 */
	public double getPrincipal(){
		return homeCost - downPayment;
	}

	/**
	 * This function gets the number of years
	 * @return [Returns the number of years for the customer]
	 * @post years = #years
	 */
	public int getYears(){
		return years;
	}


}
