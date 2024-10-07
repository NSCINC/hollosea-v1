.package(url: "https://github.com/stephencelis/SQLite.swift.git", from: "0.12.0")
import Foundation
import SQLite

// Define your database connection
let db: Connection?

// Define table and column structures
let users = Table("Users")
let userID = Expression<Int64>("id")
let userName = Expression<String>("name")
let userEmail = Expression<String>("email")
let userToken = Expression<String>("token")

let serverData = Table("ServerData")
let serverID = Expression<Int64>("id")
let serverUserID = Expression<Int64>("user_id")
let serverName = Expression<String>("server_name")
let serverStatus = Expression<String>("status")
let serverLastChecked = Expression<Date>("last_checked")

let logData = Table("LogData")
let logID = Expression<Int64>("id")
let logServerID = Expression<Int64>("server_id")
let logMessage = Expression<String>("log_message")
let logTime = Expression<Date>("log_time")

// Function to create tables in the database
func createTables() {
    do {
        // Create Users table
        try db?.run(users.create(ifNotExists: true) { table in
            table.column(userID, primaryKey: .autoincrement)
            table.column(userName)
            table.column(userEmail)
            table.column(userToken)
        })
        print("Users table created successfully")

        // Create ServerData table
        try db?.run(serverData.create(ifNotExists: true) { table in
            table.column(serverID, primaryKey: .autoincrement)
            table.column(serverUserID)
            table.column(serverName)
            table.column(serverStatus)
            table.column(serverLastChecked, defaultValue: Date())
            table.foreignKey(serverUserID, references: users, userID)
        })
        print("ServerData table created successfully")

        // Create LogData table
        try db?.run(logData.create(ifNotExists: true) { table in
            table.column(logID, primaryKey: .autoincrement)
            table.column(logServerID)
            table.column(logMessage)
            table.column(logTime, defaultValue: Date())
            table.foreignKey(logServerID, references: serverData, serverID)
        })
        print("LogData table created successfully")

    } catch {
        print("Error creating tables: \(error)")
    }
}

// Main function to run the application
func main() {
    do {
        // Open or create the database
        let path = "apollo_holofi.db"
        db = try Connection(path)
        print("Opened database successfully")
        
        // Create the tables
        createTables()

    } catch {
        print("Can't open database: \(error)")
    }
}

// Call the main function
main()
