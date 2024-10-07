import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class InvestmentRequest {
    String token;
    String planName;
    int initialInvestment;
    int amount;
    String investorAddress;
}

class SimpleNeuralNetwork {
    double[] weights;
    final int MAX_WEIGHTS = 3;

    public SimpleNeuralNetwork() {
        weights = new double[MAX_WEIGHTS];
        initializeNeuralNetwork();
    }

    public void initializeNeuralNetwork() {
        Random rand = new Random();
        for (int i = 0; i < MAX_WEIGHTS; i++) {
            weights[i] = rand.nextDouble();
        }
    }

    public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public double feedforward(double[] inputData) {
        double weightedSum = 0.0;
        for (int i = 0; i < MAX_WEIGHTS; i++) {
            weightedSum += inputData[i] * weights[i];
        }
        return sigmoid(weightedSum);
    }
}

public class InvestmentDapp {
    public static final String VALID_TOKEN = "valid_token";

    // Função para validar o token NSC
    public static boolean validateNscToken(String token) {
        return VALID_TOKEN.equals(token);
    }

    // Função para executar o contrato Lua
    public static void runLuaContract(String script, String function, String[] args) {
        StringBuilder command = new StringBuilder("lua " + script + " " + function);
        for (String arg : args) {
            command.append(" ").append(arg);
        }

        System.out.println("Executando: " + command);
        try {
            Runtime.getRuntime().exec(command.toString());
        } catch (IOException e) {
            System.out.println("Erro ao executar o contrato Lua: " + e.getMessage());
        }
    }

    // Função para adicionar um plano de investimento
    public static void addPlan(InvestmentRequest request) {
        if (!validateNscToken(request.token)) {
            System.out.println("Token NSC inválido");
            return;
        }

        String[] args = {request.planName, String.valueOf(request.initialInvestment)};
        System.out.println("Adicionando plano de investimento: " + request.planName);
        runLuaContract("investment_contract.lua", "add_plan", args);
    }

    // Função para realizar o investimento
    public static void invest(InvestmentRequest request) {
        if (!validateNscToken(request.token)) {
            System.out.println("Token NSC inválido. Investimento não autorizado.");
            return;
        }

        String[] args = {request.planName, String.valueOf(request.amount), request.investorAddress};
        System.out.println("Realizando investimento de " + request.amount + " no plano " + request.planName + " pelo investidor " + request.investorAddress);
        runLuaContract("investment_contract.lua", "invest", args);
    }

    // Função para ler arquivos .txt
    public static List<String> readTxtFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    // Função para consolidar arquivos Excel e .txt em uma única planilha
    public static void consolidateFiles(String folderPath, String outputPath) throws IOException {
        List<Path> files = Files.list(Paths.get(folderPath))
                                .filter(path -> path.toString().endsWith(".xlsx") || path.toString().endsWith(".txt"))
                                .sorted(Comparator.comparing(Path::getFileName, (p1, p2) -> {
                                    String fileName1 = p1.toString().toLowerCase();
                                    String fileName2 = p2.toString().toLowerCase();
                                    return fileName1.compareTo(fileName2);
                                }))
                                .toList();

        XSSFWorkbook consolidatedWorkbook = new XSSFWorkbook();
        Sheet sheet = consolidatedWorkbook.createSheet("Consolidated Data");
        int rowIndex = 0;

        for (Path file : files) {
            if (file.toString().endsWith(".xlsx")) {
                consolidateExcelFile(file, sheet, rowIndex);
            } else if (file.toString().endsWith(".txt")) {
                consolidateTxtFile(file, sheet, rowIndex);
            }
            rowIndex++;
        }

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            consolidatedWorkbook.write(fos);
        }
        consolidatedWorkbook.close();
        System.out.println("Consolidação concluída.");
    }

    private static void consolidateExcelFile(Path file, Sheet sheet, int rowIndex) throws IOException {
        try (FileInputStream fis = new FileInputStream(file.toFile())) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            Sheet fileSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = fileSheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Row newRow = sheet.createRow(rowIndex++);
                Iterator<Cell> cellIterator = row.cellIterator();

                int cellIndex = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Cell newCell = newRow.createCell(cellIndex++);
                    newCell.setCellValue(cell.toString());
                }
            }
            workbook.close();
        }
    }

    private static void consolidateTxtFile(Path file, Sheet sheet, int rowIndex) throws IOException {
        List<String> lines = readTxtFile(file.toString());
        for (String line : lines) {
            Row newRow = sheet.createRow(rowIndex++);
            newRow.createCell(0).setCellValue(line);
        }
    }

    public static void main(String[] args) throws IOException {
        // Inicializa a rede neural
        SimpleNeuralNetwork neuralNetwork = new SimpleNeuralNetwork();
        
        // Dados simulados para o CRM
        double[] crmData = {500.0, 100.0, 5.0};
        double result = neuralNetwork.feedforward(crmData);
        System.out.println("Resultado do processamento de CRM pela rede neural: " + result);
        
        // Simulação de um pedido de investimento
        InvestmentRequest request = new InvestmentRequest();
        request.token = "valid_token";
        request.planName = "Investment Plan A";
        request.initialInvestment = 1000;
        request.amount = 500;
        request.investorAddress = "0x123456789";

        // Adiciona um plano e executa o investimento
        addPlan(request);
        invest(request);

        // Consolida arquivos da pasta
        consolidateFiles("pasta_com_planilhas", "consolidado.xlsx");
    }
}
