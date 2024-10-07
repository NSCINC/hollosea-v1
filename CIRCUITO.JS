const fs = require('fs');
const path = require('path');
const xlsx = require('xlsx');

class ClassNamespace {
    constructor(name) {
        this.name = name;
        this.proxies = new Map();  // Mapear classes para contratos/endpoints
        this.data = {};  // Armazenar dados carregados
    }

    getName() {
        return this.name;
    }

    getProxies() {
        return this.proxies;
    }

    // Método para carregar dados de uma planilha
    loadSpreadsheet(filePath) {
        const workbook = xlsx.readFile(filePath);
        const sheetName = workbook.SheetNames[0];
        this.data.sheet = xlsx.utils.sheet_to_json(workbook.Sheets[sheetName]);
        console.log(`Dados da planilha carregados em ${this.name}:`, this.data.sheet);
    }

    // Método para carregar dados de arquivos .txt
    loadTextFile(filePath) {
        const content = fs.readFileSync(filePath, 'utf8');
        this.data.text = content;
        console.log(`Dados de texto carregados em ${this.name}:`, this.data.text);
    }

    // Obter dados carregados
    getData() {
        return this.data;
    }
}

class ClassNamespaceManager {
    constructor() {
        this.namespaces = new Map();  // Armazena todos os namespaces
    }

    // Evento emitido ao carregar uma nova biblioteca (simulação de carregar um contrato)
    libraryLoaded(path) {
        console.log(`Biblioteca carregada: ${path}`);
    }

    // Função para obter o proxy de uma classe em um namespace
    getClassProxy(namespace, className) {
        const ns = this.namespaces.get(namespace);
        if (ns) {
            return ns.getProxies().get(className);
        }
        return null;
    }

    // Função para carregar uma biblioteca (poderia ser um contrato ou um módulo de backend)
    loadLibrary(path) {
        this.libraryLoaded(path);
        // Em um cenário real, você pode carregar um módulo ou API correspondente
    }

    // Função para criar um novo namespace
    createClassNamespace(namespaceName) {
        const newNamespace = new ClassNamespace(namespaceName);
        this.namespaces.set(namespaceName, newNamespace);
        return newNamespace;
    }

    // Função para listar todos os namespaces e proxies criados
    listNamespaces() {
        console.log("Namespaces criados:");
        this.namespaces.forEach((ns, name) => {
            console.log(`Namespace: ${name}, Proxies:`, ns.getProxies());
        });
    }
}

// Exemplo de uso do sistema ajustado
const manager = new ClassNamespaceManager();

// Criar um namespace para o plano de investimento
const investmentNamespace = manager.createClassNamespace("InvestmentPlanA");
console.log(`Namespace 'InvestmentPlanA' criado`);

// Adicionar um proxy para um contrato de investimento
investmentNamespace.getProxies().set("InvestmentContract", "0xContractAddress");

// Obter o proxy do contrato
const proxy = manager.getClassProxy("InvestmentPlanA", "InvestmentContract");
console.log(`Proxy para 'InvestmentContract' no 'InvestmentPlanA': ${proxy}`);

// Carregar dados de uma planilha e um arquivo de texto no namespace
const spreadsheetPath = path.join(__dirname, 'investment_data.xlsx');
investmentNamespace.loadSpreadsheet(spreadsheetPath);

const textFilePath = path.join(__dirname, 'investment_info.txt');
investmentNamespace.loadTextFile(textFilePath);

// Listar todos os namespaces e proxies
manager.listNamespaces();

// Carregar uma biblioteca ou contrato específico (simulado)
manager.loadLibrary("/path/to/library.so");
