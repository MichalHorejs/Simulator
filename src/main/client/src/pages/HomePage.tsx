import './css/HomePage.css'
import '../index.css';

function HomePage() {


    return (
        <div className="home-container">
            <h2>Jak používat aplikaci?</h2>
            <ul>
                <li>Aby bylo možné simulátor používat, je potřeba se příhlásit. <br/>
                    Nemáte-li účet, zaregistrujte se.</li>
                <li>Přejděte na stránku simulátoru.</li>
                <li>Vyberte obtížnost a spusťe simulátor.</li>
                <li>V levém panelu se postupně začnou zobrazovat incidenty. Počet vygenerovaných incidentů závisí na obtížnosti. </li>
                <li>Postupně komunikujte pomocí chatu s civilistou a získejte informace.</li>
                <li>Získané informace vyplňte ve formuláři.</li>
                <li>Po obsloužení všech incidentů ukončete simulaci tlačítkem v pravém dolním rohu.</li>
                <li>Následně se Vám zobrazí výsledky simulace.</li>
            </ul>
        </div>
    )
}

// npm --prefix src/main/client run build
// ./gradlew bootRun
export default HomePage;