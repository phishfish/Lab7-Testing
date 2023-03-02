package cpsc2150.banking.models;

/**
 *
 *@invariants
 * MIN_YEARS <= years <= MAX_YEARS
 * homeCost >= 0
 * downPayment >= 0
 *
 *@correspondence
 * rate = getRate()
 * monthllyPayment = getMonthlyPay()
 * principle = getPrincipal()
 */

public class Mortgage extends AbsMortgage implements IMortgage{

	private double homeCost;
	private double downPayment;
	private int years;
	private ICustomer customer;
	public Mortgage(double cost, double down, int length, ICustomer person){
		homeCost = cost;
		downPayment = down;
		years = length;
		customer = person;
	}

	public boolean loanApproved(){
		if(getRate() > .1){
			return false;
		}

		if(downPayment < homeCost * MIN_PERCENT_DOWN){
			return false;
		}

		if((customer.getMonthlyPay() * years)/(customer.getIncome() * years) > DTOITOOHIGH){
			return false;
		}

		return true;
	}

	public double getPayment(){
		double interestRate = getRate();
		return (interestRate/12 * getPrincipal())/(1 - Math.pow(1 + interestRate/12, -1 * years * 12));
	}

	public double getRate(){
		double apr = BASERATE;
		if(years < MAX_YEARS)
			apr += GOODRATEADD; 
		else
			apr += 0.01;

		if(homeCost * PREFERRED_PERCENT_DOWN > downPayment)
			apr += GOODRATEADD;

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

	public double getPrincipal(){
		return homeCost - downPayment;
	}
public int getYears(){
		return years;
	}


}
