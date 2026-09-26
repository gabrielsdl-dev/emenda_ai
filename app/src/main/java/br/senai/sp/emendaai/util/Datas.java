package br.senai.sp.emendaai.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public final class Datas {

    private static final Locale BR = new Locale("pt", "BR");
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Datas() {
    }

    /** Devolve null se o texto nao for uma data valida, em vez de derrubar o app. */
    public static LocalDate paraData(String iso) {
        if (iso == null) {
            return null;
        }
        try {
            return LocalDate.parse(iso, ISO);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /** "07" */
    public static String dia(String iso) {
        LocalDate d = paraData(iso);
        return d == null ? "--" : String.format(BR, "%02d", d.getDayOfMonth());
    }

    /** "SET" */
    public static String mesCurto(String iso) {
        LocalDate d = paraData(iso);
        if (d == null) {
            return "---";
        }
        String mes = d.getMonth().getDisplayName(TextStyle.SHORT, BR);
        return mes.replace(".", "").toUpperCase(BR);
    }

    /** "segunda-feira" */
    public static String diaDaSemana(String iso) {
        LocalDate d = paraData(iso);
        return d == null ? "" : d.getDayOfWeek().getDisplayName(TextStyle.FULL, BR);
    }

    /** "07/09/2026" */
    public static String porExtenso(String iso) {
        LocalDate d = paraData(iso);
        return d == null ? "" : d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /** Feriado que cai numa terca ou numa quinta abre espaco para emenda. */
    public static boolean ehEmenda(String iso) {
        LocalDate d = paraData(iso);
        if (d == null) {
            return false;
        }
        DayOfWeek dow = d.getDayOfWeek();
        return dow == DayOfWeek.TUESDAY || dow == DayOfWeek.THURSDAY;
    }

    /** "faltam 42 dias", "e amanha", "ja passou". */
    public static String contagem(String iso) {
        LocalDate d = paraData(iso);
        if (d == null) {
            return "";
        }
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), d);
        if (dias == 0) {
            return "é hoje";
        }
        if (dias == 1) {
            return "é amanhã";
        }
        if (dias < 0) {
            return "já passou";
        }
        return "faltam " + dias + " dias";
    }
}
