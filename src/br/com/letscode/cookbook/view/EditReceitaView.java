package br.com.letscode.cookbook.view;

import br.com.letscode.cookbook.domain.Ingrediente;
import br.com.letscode.cookbook.domain.Receita;
import br.com.letscode.cookbook.domain.Rendimento;
import br.com.letscode.cookbook.enums.Categoria;
import br.com.letscode.cookbook.enums.TipoMedida;
import br.com.letscode.cookbook.enums.TipoRendimento;

import java.util.Locale;

public class EditReceitaView {
    private Receita receita;

    public EditReceitaView(Receita receita) {
        this.receita = new Receita(receita);
    }

    public Receita edit() {
        do {
            new ReceitaView(receita).fullView(System.out);
        } while (showMenuReceita());

        String opcao = ConsoleUtils.getUserOption("Você deseja salvar a receita " + receita.getNome() + "?\nS - Sim   N - Não", "S", "N");
        if ("S".equalsIgnoreCase(opcao)) {
            return receita;
        } else {
            return null;
        }
    }

    private boolean showMenuReceita() {
        String[] options = new String[9];
        StringBuilder sb = new StringBuilder("#".repeat(100));
        sb.append("%n").append("Informe a alteracao que deseja realizar");
        sb.append("%n").append("#".repeat(100));
        sb.append("%n").append("  1 : Alterar Nome  %n").append("  2 : Alterar Categoria  %n");
        sb.append("  3 : Alterar Tempo de preparo  %n").append("  4 : Alterar Rendimento  %n");
        sb.append("  5 : Adicionar Ingrediente  %n");
        options[0] = "1";
        options[1] = "2";
        options[2] = "3";
        options[3] = "4";
        options[4] = "5";
        if (receita.getIngredientes().size() != 0) {
            sb.append("  6 : Remover Ingrediente  %n");
            options[5] = "6";
        }
        sb.append("  7 : Adicionar Preparo  %n");
        options[6] = "7";
        if (receita.getPreparo().size() != 0) {
            sb.append("  8 : Remover Preparo  %n");
            options[7] = "8";
        }
        sb.append("  # ").append("# ".repeat(48)).append("%n");
        sb.append("  X : Sair  %n");
        options[8] = "X";
        sb.append("#".repeat(100)).append("%n");

        String opcao = ConsoleUtils.getUserOption(sb.toString(), options).toUpperCase(Locale.getDefault());
        switch (opcao) {
            case "1":
                editNome();
                break;
            case "2":
                editCategoria();
                break;
            case "3":
                editTempoPreparo();
                break;
            case "4":
                editRendimento();
                break;
            case "5":
                addIngrediente();
                break;
            case "6":
                delIngrediente();
                break;
            case "7":
                addPreparo();
                break;
            case "8":
                delPreparo();
                break;
            case "X":
                System.out.println("Obrigado!!");
                return false;
            default:
                System.out.println("Opção inválida!!!");
        }
        return true;
    }

    private void editNome() {
        String name = ConsoleUtils.getUserInput("Qual o novo nome da receita?");
        if ((name != null) && (!name.isBlank())) {
            receita.setNome(name);
        }
    }

    private void editTempoPreparo() {
        do {
            String time = ConsoleUtils.getUserInput("Qual o novo tempo de preparo da receita?");
            if (!time.isBlank()) {
                try {
                    double value = Double.parseDouble(time);
                    receita.setTempoPreparo(value);
                    return;
                } catch (NullPointerException | NumberFormatException e) {
                    System.out.println("Tempo de preparo invalido.");
                }
            } else {
                break;
            }
        } while (true);
    }

    private void editCategoria() {
        StringBuilder sb = new StringBuilder("Qual a nova categoria da receita?\n");
        String[] options = new String[Categoria.values().length];
        for (int i = 0; i < options.length; i++) {
            options[i] = String.valueOf(i);
            sb.append(String.format("%d - %s%n", i, Categoria.values()[i]));
        }
        String opcao = ConsoleUtils.getUserOption(sb.toString(), options);
        Categoria categoria = null;
        for (int i = 0; i < options.length; i++) {
            if (opcao.equalsIgnoreCase(options[i])) {
                categoria = Categoria.values()[i];
                break;
            }
        }
        receita.setCategoria(categoria);
    }

    public void editRendimento() {
        int valueMin;
        int valueMax;
        do {
            String min = ConsoleUtils.getUserInput("Qual o rendimento minimo da receita?");
            try {
                if (min != null) {
                    valueMin = Integer.parseInt(min);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Rendimento minimo invalido.");
            }
        } while (true);
        do {
            String max = ConsoleUtils.getUserInput("Qual o rendimento maximo da receita?");
            try {
                if (max != null) {
                    valueMax = Integer.parseInt(max);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Rendimento maximo invalido.");
            }
        } while (true);

        TipoRendimento tipoRendimento = editTipoRendimento();
        if (valueMin == valueMax) {
            receita.setRendimento(new Rendimento(valueMin, tipoRendimento));
        } else {
            receita.setRendimento(new Rendimento(valueMin, valueMax, tipoRendimento));
        }
    }

    private TipoRendimento editTipoRendimento() {
        StringBuilder sb = new StringBuilder("Qual o tipo de rendimento da receita?\n");
        String[] options = new String[TipoRendimento.values().length];
        for (int i = 0; i < options.length; i++) {
            options[i] = String.valueOf(i);
            sb.append(String.format("%d - %s%n", i, TipoRendimento.values()[i]));
        }
        String opcao = ConsoleUtils.getUserOption(sb.toString(), options);
        TipoRendimento tipoRendimento = null;
        for (int i = 0; i < options.length; i++) {
            if (opcao.equalsIgnoreCase(options[i])) {
                tipoRendimento = TipoRendimento.values()[i];
                break;
            }
        }
        return tipoRendimento;
    }

    private void addIngrediente() {
        double quantidade;
        String name = ConsoleUtils.getUserInput("Qual o nome do ingrediente?");
        if (name.isBlank()) {
            return;
        }

        do {
            String qtd = ConsoleUtils.getUserInput("Qual a quantidade do ingrediente?");
            try {
                if (qtd != null) {
                    quantidade = Double.parseDouble(qtd);
                    break;
                }
            } catch (NullPointerException | NumberFormatException e) {
                System.out.println("Quantidade do ingrediente invalida.");
            }
        } while (true);

        TipoMedida tipoMedida = addTipoMedida();
        receita.getIngredientes().add(new Ingrediente(name, quantidade, tipoMedida));
    }

    private TipoMedida addTipoMedida() {
        StringBuilder sb = new StringBuilder("Qual o tipo de medida do ingrediente?\n");
        String[] options = new String[TipoMedida.values().length];
        for (int i = 0; i < options.length; i++) {
            options[i] = String.valueOf(i);
            sb.append(String.format("%d - %s%n", i, TipoMedida.values()[i]));
        }
        String opcao = ConsoleUtils.getUserOption(sb.toString(), options);
        TipoMedida tipoMedida = null;
        for (int i = 0; i < options.length; i++) {
            if (opcao.equalsIgnoreCase(options[i])) {
                tipoMedida = TipoMedida.values()[i];
                break;
            }
        }
        return tipoMedida;
    }

    private void delIngrediente() {
        StringBuilder sb = new StringBuilder("Qual o ingrediente que você quer remover?\n");
        String[] options = new String[receita.getIngredientes().size()+1];
        for (int i = 0; i < options.length-1; i++) {
            options[i] = String.valueOf(i);
            sb.append(String.format("%d - %s%n", i, receita.getIngredientes().get(i).toString()));
        }
        options[options.length-1] = "X";
        sb.append("X - Sair");
        String opcao = ConsoleUtils.getUserOption(sb.toString(), options);
        if (!"X".equalsIgnoreCase(opcao)) {
            for (int i = 0; i < options.length - 1; i++) {
                if (opcao.equalsIgnoreCase(options[i])) {
                    receita.getIngredientes().remove(i);
                    break;
                }
            }
        }
    }

    private void addPreparo() {
        String preparo = ConsoleUtils.getUserInput("Entre com o passo de preparo a incluir");
        if (preparo.isBlank()) {
            return;
        }
        if (receita.getPreparo().size() == 0) {
            receita.getPreparo().add(preparo);
        } else {
            StringBuilder sb = new StringBuilder("Em que ponto do preparo você quer adicionar esse passo?\n");
            String[] options = new String[receita.getPreparo().size()+1];
            for (int i = 0; i < options.length-1; i++) {
                options[i] = String.valueOf(i);
                sb.append(String.format("%d - %s%n", i, receita.getPreparo().get(i)));
            }
            options[options.length-1] = String.valueOf(options.length-1);
            sb.append(String.format("%d - %s%n", options.length-1, "+++Fim da lista+++"));
            String opcao = ConsoleUtils.getUserOption(sb.toString(), options);
            for (int i = 0; i < options.length; i++) {
                if (opcao.equalsIgnoreCase(options[i])) {
                    receita.getPreparo().add(i, preparo);
                    break;
                }
            }
        }
    }

    private void delPreparo() {
        StringBuilder sb = new StringBuilder("Qual o passo do preparo que você quer remover?\n");
        String[] options = new String[receita.getPreparo().size()+1];
        for (int i = 0; i < options.length-1; i++) {
            options[i] = String.valueOf(i);
            sb.append(String.format("%d - %s%n", i, receita.getPreparo().get(i)));
        }
        options[options.length-1] = "X";
        sb.append("X - Sair");
        String opcao = ConsoleUtils.getUserOption(sb.toString(), options);
        if (!"X".equalsIgnoreCase(opcao)) {
            for (int i = 0; i < options.length - 1; i++) {
                if (opcao.equalsIgnoreCase(options[i])) {
                    receita.getPreparo().remove(i);
                    break;
                }
            }
        }
    }
}
