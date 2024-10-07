#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_INVESTORS 100 // Defining a limit for the number of investors

typedef struct {
    char *investors[MAX_INVESTORS];
    int authorizedInvestors[MAX_INVESTORS]; // 1 for authorized, 0 for unauthorized
    long balances[MAX_INVESTORS]; // Balance of each investor
    long investedAmount[MAX_INVESTORS]; // Amount invested by each investor
    int investorCount; // Count of investors
} InvestmentContract;

void invest(InvestmentContract *contract, const char *investor, long amount) {
    if (amount <= 0) {
        printf("Investment amount must be greater than zero\n");
        return;
    }

    int investorIndex = -1;

    // Check if the investor is in the list of investors
    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            investorIndex = i;
            break;
        }
    }

    if (investorIndex == -1) {
        printf("Investor '%s' not found in the contract\n", investor);
        return;
    }

    // Check if the investor is authorized
    if (!contract->authorizedInvestors[investorIndex]) {
        printf("Investor '%s' is not authorized to invest\n", investor);
        return;
    }

    // Check if the balance is sufficient
    if (amount > contract->balances[investorIndex]) {
        printf("Insufficient balance for investor '%s'. Current balance: %ld\n", investor, contract->balances[investorIndex]);
        return;
    }

    // Update balances and invested amounts
    contract->balances[investorIndex] -= amount;
    contract->investedAmount[investorIndex] += amount;
    printf("Investment of %ld made by '%s'\n", amount, investor);
}

// Function to initialize the investment contract and example investors
void initializeContract(InvestmentContract *contract) {
    contract->investorCount = 0;

    // Add investors if there is space
    if (contract->investorCount < MAX_INVESTORS) {
        contract->investors[contract->investorCount] = strdup("Alice");
        contract->authorizedInvestors[contract->investorCount] = 1; // Authorized
        contract->balances[contract->investorCount] = 1000; // Initial balance
        contract->investedAmount[contract->investorCount] = 0; // Initial investment
        contract->investorCount++;
    }

    if (contract->investorCount < MAX_INVESTORS) {
        contract->investors[contract->investorCount] = strdup("Bob");
        contract->authorizedInvestors[contract->investorCount] = 0; // Not authorized
        contract->balances[contract->investorCount] = 500; // Initial balance
        contract->investedAmount[contract->investorCount] = 0; // Initial investment
        contract->investorCount++;
    }

    if (contract->investorCount < MAX_INVESTORS) {
        contract->investors[contract->investorCount] = strdup("Charlie");
        contract->authorizedInvestors[contract->investorCount] = 1; // Authorized
        contract->balances[contract->investorCount] = 1500; // Initial balance
        contract->investedAmount[contract->investorCount] = 0; // Initial investment
        contract->investorCount++;
    }
}

// Function to free allocated memory for investors
void freeContract(InvestmentContract *contract) {
    for (int i = 0; i < contract->investorCount; i++) {
        free(contract->investors[i]);
    }
}

int main() {
    InvestmentContract contract;
    initializeContract(&contract);

    // Testing the invest function
    invest(&contract, "Alice", 200);   // Valid investment
    invest(&contract, "Bob", 100);     // Unauthorized investor
    invest(&contract, "Charlie", 2000); // Insufficient balance
    invest(&contract, "Dave", 100);    // Investor not found

    // Free allocated memory
    freeContract(&contract);
    
    return 0;
}
