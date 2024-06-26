package unibo.citysimulation.model.business.api;

import unibo.citysimulation.model.business.impl.Business;

/**
 * The HandleEmployye interface defines methods for handling employees in a business.
 */
public interface EmploymentOfficeBehavior {
    /**
     * Handles the firing of employees for the specified business.
     * For each employee that should be fired, adds the person to the employment office's disoccupied people list
     * and fires the employee from their business.
     * 
     * @param business The business for which to handle employee firing.
     * @param hiredCount The number of people hired previously.
     */
    void handleEmployeeFiring(Business business, int hiredCount);
    /**
     * Handles the hiring of employees for the specified business.
     * Hires a minimum of 4 employees from the employment office's disoccupied people list 
     * or up to the maximum number of employees allowed for the business.
     * 
     * @param business The business for which to handle employee hiring.
     * @return The number of people hired.
     */
    int handleEmployeeHiring(Business business);
    /**
     * Handles the payment of employees for the specified business.
     * Pays each employee in the business the amount of money they are owed.
     * 
     * @param business The business for which to handle employee payment.
     */
    void handleEmployyePay(Business business);
}
