public class Principal {
    public static void main(String[] args) {
        System.out.println(ComponenteCurricular.cadastrarComponente("234567", "banco de dados", 60, "obrigatoria", 4));
        System.out.println(Professor.cadastrarProfessor("234567", "Mabel", "doutorado", "mabel@gmail.com", "engenharias"));
        Professor prof = new Professor();
        prof.HorasAulaSemanal();
        
    }
}
