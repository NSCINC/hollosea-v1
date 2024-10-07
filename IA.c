#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sqlite3.h>

#define MAX_NAME_LENGTH 100
#define MAX_STATUS_LENGTH 50
#define MAX_EMAIL_LENGTH 100

// Structure to store server data
typedef struct {
    char name[MAX_NAME_LENGTH];
    char status[MAX_STATUS_LENGTH];
    char ownerEmail[MAX_EMAIL_LENGTH];
} Server;

// Function to open the database connection
sqlite3* connectDatabase() {
    sqlite3 *db;
    int rc = sqlite3_open("apollo_holofi.db", &db);

    if (rc) {
        printf("Error opening database: %s\n", sqlite3_errmsg(db));
        return NULL;
    } else {
        printf("Database opened successfully.\n");
        return db;
    }
}

// Function to initialize the database, creating the Servers table
void initializeDatabase(sqlite3 *db) {
    const char *sql = "CREATE TABLE IF NOT EXISTS Servers ("
                      "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                      "name TEXT NOT NULL,"
                      "status TEXT NOT NULL,"
                      "owner_email TEXT NOT NULL);";

    char *errMsg = NULL;
    int rc = sqlite3_exec(db, sql, NULL, NULL, &errMsg);

    if (rc != SQLITE_OK) {
        printf("Error creating table: %s\n", errMsg);
        sqlite3_free(errMsg);
    } else {
        printf("Servers table created or already exists.\n");
    }
}

// Function to insert a server into the database
void insertServer(sqlite3 *db, Server server) {
    const char *sql = "INSERT INTO Servers(name, status, owner_email) VALUES(?, ?, ?);";
    sqlite3_stmt *stmt;

    if (sqlite3_prepare_v2(db, sql, -1, &stmt, NULL) != SQLITE_OK) {
        printf("Failed to prepare statement: %s\n", sqlite3_errmsg(db));
        return;
    }

    sqlite3_bind_text(stmt, 1, server.name, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 2, server.status, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 3, server.ownerEmail, -1, SQLITE_STATIC);

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        printf("Error inserting server: %s\n", sqlite3_errmsg(db));
    } else {
        printf("Server '%s' inserted successfully.\n", server.name);
    }

    sqlite3_finalize(stmt);
}

// Function to read server data from a CSV file
void readCsvData(sqlite3 *db, const char *filePath) {
    FILE *file = fopen(filePath, "r");
    if (!file) {
        printf("Error opening CSV file.\n");
        return;
    }

    char line[256];
    while (fgets(line, sizeof(line), file)) {
        Server server;

        // Extracting server data
        char *token = strtok(line, ",");
        if (token != NULL) {
            strncpy(server.name, token, MAX_NAME_LENGTH - 1);
            server.name[MAX_NAME_LENGTH - 1] = '\0'; // Null-terminate
        }

        token = strtok(NULL, ",");
        if (token != NULL) {
            strncpy(server.status, token, MAX_STATUS_LENGTH - 1);
            server.status[MAX_STATUS_LENGTH - 1] = '\0'; // Null-terminate
        }

        token = strtok(NULL, ",");
        if (token != NULL) {
            strncpy(server.ownerEmail, token, MAX_EMAIL_LENGTH - 1);
            server.ownerEmail[MAX_EMAIL_LENGTH - 1] = '\0'; // Null-terminate
        }

        // Insert the server into the database
        insertServer(db, server);
    }

    fclose(file);
}

// Main function
int main() {
    sqlite3 *db = connectDatabase();
    
    if (db == NULL) {
        return 1; // Exit if database connection failed
    }

    // Initialize the database and create the Servers table
    initializeDatabase(db);

    // Read data from the CSV file and insert into the database
    readCsvData(db, "servers_data.csv");

    // Close the database
    sqlite3_close(db);
    
    return 0; // Return success
}
