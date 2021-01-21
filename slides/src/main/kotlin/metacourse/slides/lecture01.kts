import io.kotex.beamer.frame
import io.kotex.core.*
import metacourse.slides.createPreamble
import java.io.File

val doc = document(createPreamble("Введение")) {
    makeTitle()

    section("Введение") {
        frame("Функциональный подход - А. П. Ершов") {
            +"""
            Пусть у нас есть программа $ p $, вычисляющая функцию $ f(X, Y) $ (если $ X $ – переменная, 
            то $ x $ – ее значение). Найти программу $ p_X $ , вычисляющую функцию $ g(Y) = f (x, Y) $. 
            Программу $ p_X $ назовем ${"остаточной программой".bold()} или ${"проекцией".bold()} $ p $ на $ x $, 
            а процесс ее получения – смешанным вычислением, поскольку в нем, по-видимому, чисто вычислительные шаги, 
            связанные с обработкой информации $ x $, перемежаются с шагами конструирования (генерации) остаточной 
            программы.
            """
        }

        frame("Операционный подход - А. П. Ершов") {
            +"""
            Пусть программа p задает некоторое множество C элементарных вычислительных актов и пусть задано разбиение
            $ \mu $ этого множества на два составляющих $ \mu : C = C' \cup C'' $. Одно из них,
            C' , назовем \textbf{множеством допустимых вычислений}, а другое, C'', – 
            ${"множеством задержанных вычислений".bold()}. Смешанные вычисления над программой $ p $ при разбиении – это
            процесс выполнения допустимых вычислений и формирования остаточной программы $ p_{\mu} $, 
            имеющей своим множеством вычислений множество $ C'' $ задержанных вычислений.
            """
        }

        frame("Пример: операционный подход") {
            +"Императивная программа для вычисления $ x^n $:"

            verbatim(
                """
                y = 1;
                while (n > 0) {
                    if (isEven(n)) {
                        n = n / 2;
                        x = x * 2;
                    }
                    n = n - 1;
                    y = y * x;
                }
            """
            )
        }

        frame("Вычисления при $ n = 5 $") {
            verbatim(
                """
              y = 1;  n > 0; isEven(n); n = n - 1; y = y * x; (n = 4)
                      n > 0; isEven(n); n = n / 2; x = x * x; (n = 2)
                      n > 0; isEven(n); n = n / 2; x = x * x; (n = 1)
                      n > 0; isEven(n); n = n - 1; y = y * x; (n = 0)
                      n > 0.
            """
            )
        }

        frame("Частичное вычисление при $ n = 5 $") {
            verbatim(
                """
              y = 1;  n > 0; isEven(n); n = n - 1; 
                      n > 0; isEven(n); n = n / 2;
                      n > 0; isEven(n); n = n / 2; 
                      n > 0; isEven(n); n = n - 1;
                      n > 0.
            """
            )
            +"Остаточная программа:"
            verbatim("y = 1 * x; x = x * x; x = x * x; y = y * x")
        }

        frame("Пример: функциональный подход") {
            +"""
            Такой подход А. П. Ершов также называл ${"трансформационным".bold()},
            т.к. в его основе лежат известные правила преобразования рекурсивых программ:
            """
            itemize {
                -"Конкретизация (подстановка числа в обе части определения функции)"
                -"Упрощение (замена терма его значением и устранение альтернативы у условного терма с известным условием)"
                -"Разъединение (замена обращения к функции ее правой частью)"
                -"Устранение ненужного (неиспользуемого) определения функции"
            }
        }

        frame("Функциональная программа для $ x ^ n $") {
            verbatim(
                """
              p(x, n) | n = 0     -> 1
                      | isEven(n) -> p(x, n / 2) ^ 2
                      | otherwise -> x * p (x, n - 1)
            """
            )
        }

        frame("Частичные вычисления для $ n = 5 $: шаг 1") {
            +"""
            Обозначим для краткости правую часть определения $ p(x, n) $ через $ T(x, n) $ и покажем, как 
            последовательным применением преобразований можно получить из $ p(x, n) $ остаточную программу для 
            $ x^5 $, т.е. $ p(x, 5) $
            """

            verbatim("1) p(x, n) = T(x, n).")

            +"Конкретизация 1) при $ n = 5 $:"

            verbatim(
                """
            1) p(x, n) = T(x, n),
            2) p(x, 5) = T(x, 5).   
            """
            )
        }

        frame("Частичные вычисления для $ n = 5 $: шаг 2 и 3") {
            +"Упрощение 2):"
            verbatim(
                """
            1) p(x, n) = T(x, n), 
            2) p(x, 5) = x * p(x, 4).               
            """
            )
            +"Конкретизация 1) при $ n = 4 $ и последующее упрощение:"
            verbatim(
                """
            1) p(x, n) = T(x, n),
            2) p(x, 5) = x * p(x, 4),
            3) p(x, 4) = p(x, 2) ^ 2.
            """
            )
        }

        frame("Частичные вычисления для $ n = 5 $: шаг 4 и 5") {
            +"Конкретизация 1) при $ n = 2 $ и последующее упрощение:"
            verbatim(
                """
            1) p(x, n) = T(x, n),
            2) p(x, 5) = x * p(x, 4),
            3) p(x, 4) = p(x, 2) ^ 2,
            4) p(x, 2) = p(x, 1) ^ 2.
            """
            )
            +"Конкретизация 1) при $ n = 1 $ и последующее упрощение:"
            verbatim(
                """
            1) p(x, n) = T(x, n),
            2) p(x, 5) = x * p(x, 4),
            3) p(x, 4) = p(x, 2) ^ 2,
            4) p(x, 2) = p(x, 1) ^ 2,
            5) p(x, 1) = x * p(x, 0),
            6) p(x, 0) = 1.
            """
            )
        }

        frame("Частичные вычисления для $ n = 5 $: шаг 6 и 7") {
            +"Устранение ненужного 1) и разъединение 5) и 6):"
            verbatim(
                """
            1) p(x, 5) = x * p (x, 4),
            2) p(x, 4) = p(x, 2) ^ 2,
            3) p(x, 2) = p(x, 1) ^ 2,
            4) p(x, 1) = x * 1,
            5) p(x, 0) = 1.
            """
            )
            +"Устранение ненужного 5) и разъединение 3) и 4):"
            verbatim(
                """
            1) p(x, 5) = x * p(x, 4),
            2) p(x, 4) = p(x, 2) ^ 2,
            3) p(x, 2) = (x * 1) ^ 2,
            4) p(x, 1) = x * 1.
            """
            )
        }

        frame("Частичные вычисления для $ n = 5 $: шаг 8, 9 и 10") {
            +"Устранение ненужного 4) и разъединение 2) и 3):"
            verbatim(
                """
            1) p(x, 5) = x * p(x, 4),
            2) p(x, 4) = ((x * 1) ^ 2) ^ 2,
            3) p(x, 2) = (x * 1) ^ 2,
            """
            )
            +"Устранение ненужного 3) и разъединение 1) и 2):"
            verbatim(
                """
            1) p(x, 5) = x * ((x * 1) ^ 2) ^ 2,
            2) p(x, 4) = ((x * 1) ^ 2) ^ 2.
            """
            )
            +"Устранение ненужного 2):"
            verbatim(
                """
            1) p(x, 5) = x * ((x * 1) ^ 2) ^ 2.
            """
            )
        }

        frame("Применение частичных вычислений") {
            +"""Если нам нужно проводить одно и то же вычисление много раз с фиксированным аргументом, 
            то частичные вычисление потенциально обеспечивают существенный прирост производительности. 
            Возможная область применения частичных вычислений \cite{Jones93}:"""
            itemize {
                -"компьютерная графика: специализация по сцене,"
                -"запросы к БД,"
                -"..."
                -"преобразование программ"
            }
        }
    }

    section("Специализатор и его свойства") {

        frame("Язык программирования") {
            +"""Формально определение языка программирования (А. П. Ершов):
            \[
            L = (P, D, S),
            \]
            где"""
            itemize {
                -"""$ P = \{p\} $ - множество программ,"""
                -"""$ D = \{d\}$ - предметная область,"""
                -"""$ S: P \times D \to D$ - семантика."""
            }
        }

        frame("Интерпретатор") {
            +"""Интерпретатором языка $ L $ назвается программа $ int: P \times D \to D$ написанная на языке $ L_0 $ для 
            которой выполняется следующее свойство \cite{AbramovMeta}:
    
            \[
            int(p, d) = p(d), \forall p \in P, \forall d \in D
            \]
            где $ p(d) $ это результат выполнения программы $ p $ на данных $ d $.
            Таким образом для того, чтобы задать семантику языка, достаточно просто предъявить интерпретатор этого языка."""
        }

        frame("Формальное определение специализатора") {
            +"""Специализатор $ spec $ языка $ S $ - это такая программа на языке $ T $, что для любой программы $ p $ на языке 
            $ S $, от двух формальных параметров $ X $ и $ Y $, и заданного значения $ x \in D'$ (здесь $ D = D' \times D''$) и 
            $\forall y \in D''$, выполняется следующее:
            \[
            spec(p, x)(y) = p(x, y).
            \]
            Таким образом $ spec(p, x) $ представляет собой остаточную программу $ p_x $ или, \textbf{проекцию}."""
        }

        frame("Случай $ S = L_0 $") {
            +"""Особый интерес представляет случай, когда специализатор $ spec $ можно применить к интерпретатору $ int $:
            \begin{equation}\label{eqn:Futamura1}
            spec(int, p)(d) = int(p, d), \forall p \in P, \forall d \in D
            \end{equation}
            В этом случае $ spec(int, p) $ делает то же самое, что и программа $ p $, но уже на языке $ S $, т.е. 
            $ spec(int, p)$ - это \textbf{результат компиляции} $ p $ с языка $ L $ в язык $ S $.
            
            Равенство (\ref{eqn:Futamura1}) называется \textbf{первой проекцией Футамуры} \cite{Futamura71}."""
        }

        frame("Метациклический случай") {
            +"""Особый интерес представляет случай, когда $ S = T = L_0 $, тогда можно применять $ spec $ к 
            самому себе также как и к $ int $:
            \begin{equation}\label{eqn:Futamura2}
            spec(spec, int)(p)(d) = spec(int, p)(d), \forall p \in P, \forall d \in D
            \end{equation}
            Нетрудно заметить, что $ spec(spec, int) $ представляет собой компилятор языка
            $ L $ в язык $ T $.
    
            Равенство (\ref{eqn:Futamura2}) называется ${"второй проекцией Футамуры".bold()}."""
        }

        frame("Третья проекция Футамуры") {
            +"""Если продолжить цепочку, то получим ${"генератор компиляторов".bold()}:
            \begin{equation}
            spec(spec, spec)(int)(p)(d) = spec(spec, int)(p)(d),
            \end{equation}
            $\forall int \in \mathcal{I}, \forall p \in P, \forall d \in D$,
            где $\mathcal{I}$ - это множество всех интерпретаторов, написанных на языке $ S $."""
        }
    }



    section("Обобщения и частные случаи") {
        frame("Суперкомпиляция - С. М. Абрамов") {
            +"""\textbf{Суперкомпиляция}—один из разделов метавычислений, совокупность понятий, 
            методов и алгоритмов метавычислений, позволяющих выполнять глубокие преобразования 
            программ на языке $ L $ \cite{AbramovMetaDop}."""

            +"""Аргументами суперкомпилятора являются: $ p \in P $ — программа на языке $ L $ и $\mathcal{C}$—класс, 
            обобщенные данные для $ p $."""

            +"""Таким образом, суперкомпилятор вычисляет программу $ p_{\mathcal{C}} $, которая является результатом 
            специализации $ p $ на класс $ \mathcal{C} $, то есть специальной версией программы $ p $ для случая, когда 
            входные данные имеют вид $ d \in \mathcal{C} $."""
        }

        frame("Специализация программ") {
            +"""Как следует из определения: специализация программ - это \textbf{частный случай} суперкомпиляции, 
            когда в качестве класс $\mathcal{C}$ мы берем множество из одного элемента."""
        }

        frame("Виртуальные машины и промежуточные представления") {
            +"""Пусть существует виртуальная машина, которая умеет эффективно выполнять какой-то промежуточный язык 
            $ IR $, и есть компилятор языка выского уровня $ K $ в $ IR $. Тогда достаточно иметь специализатор $ IR $,
            чтобы можно было реализовывать интерпретаторы на языке $ K $ и получать компилятор автоматически.
            \[
            comp_{L \to IR}(p) = spec_{IR}(comp_{K \to IR}(int_L), p), \forall p \in P,
            \]
            где $ P $ - множество программ на языке $ L $."""
        }
    }

    section("Современная реализация") {
        frame("Graal и Truffle") {
            itemize {
                -"Реализует только первую проекцию Футамуры"
                -"В качестве языка реализации использует Java"
                -"Имеет механику для коммуникации между интерпретатором и специализатором"
                -"Использует спекулятивные оптимизации с откатом"
                -"Для динамических языков во время специализации собирается информация о типах"
                -"Показывает отличные результаты in production"
                itemize {
                    -"0.83x относительно V8 для JavaScript \\cite{Truffle17}"
                    -"3.8x относительно JRuby"
                    -"5x относительно GNU R"
                }
            }
        }
//
//        \begin{frame}[standout]
//        Вопросы?
//        \end{frame}
//
//        \appendix
//
//        \begin{frame}[allowframebreaks]{Литература}
//
//        \bibliography{fpure2019}
//        \bibliographystyle{plain}
//
//        \end{frame}
    }

}

doc.write("../../../../../tex/lecture01.tex")