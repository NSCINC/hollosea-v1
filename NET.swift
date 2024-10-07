import Foundation
import Web3
dependencies: [
    .package(url: "https://github.com/matter-labs/web3swift.git", from: "2.3.0")
]

let infuraMainnetURL = "https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID" // Replace with your Infura Project ID

// Function to check the current block number
func checkBlockNumber() {
    // Create a Web3 instance
    guard let url = URL(string: infuraMainnetURL) else {
        print("Invalid URL.")
        return
    }
    
    let web3 = try! Web3(rpcURL: url.absoluteString)

    // Fetch the current block number
    web3.eth.getBlockNumber { (result) in
        switch result {
        case .success(let blockNumber):
            print("Current block number: \(blockNumber)")
        case .failure(let error):
            print("Error fetching block number: \(error.localizedDescription)")
        }
    }
}

// Call the function to check the block number
checkBlockNumber()

// Keep the program running to wait for the async response
RunLoop.main.run()
