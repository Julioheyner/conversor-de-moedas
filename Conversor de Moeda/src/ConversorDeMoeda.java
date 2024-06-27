//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConversorDeMoeda {
    private static final String API_KEY = "Chave API a incluir";

    public static void main(String[] args) {
        exibirMenu();
    }

    public static void exibirMenu() {
        System.out.println("*** CONVERSOR DE MOEDAS ***");
        System.out.println("Escolha a moeda a converter:");
        System.out.println("1. USD - Dólar Americano");
        System.out.println("2. ARS - Peso Argentino");
        System.out.println("3. COP - Peso Colombiano");
        System.out.println("4. BRL - Real Brasileiro");
        System.out.println("5. Sair");
        System.out.print("Digite o código da moeda de origem: ");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String moedaOrigem = reader.readLine().toUpperCase();
            System.out.print("Digite o código da moeda de destino: ");
            String moedaDestino = reader.readLine().toUpperCase();

            System.out.print("Digite o valor a converter: ");
            double valor = Double.parseDouble(reader.readLine());

            double valorConvertido = converterMoeda(moedaOrigem, moedaDestino, valor);
            System.out.println("Valor convertido: " + valorConvertido + " " + moedaDestino);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao processar entrada. Certifique-se de inserir um valor válido!.");
        }
    }

    public static double converterMoeda(String moedaOrigem, String moedaDestino, double valor) {
        try {
            String url = "https://open.er-api.com/v6/latest/" + moedaOrigem + "?apikey=" + API_KEY;

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            // Parse do JSON para obter a taxa de câmbio
            String jsonString = response.toString();
            double taxaCambio = Double.parseDouble(jsonString.split("\"")[5]);

            // Converter para a moeda de destino
            url = "https://open.er-api.com/v6/latest/" + moedaDestino + "?apikey=" + API_KEY;

            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            jsonString = response.toString();
            double taxaCambioDestino = Double.parseDouble(jsonString.split("\"")[5]);

            return (valor * taxaCambio) / taxaCambioDestino;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao converter moeda: " + e.getMessage());
            return 0;
        }
    }
}
