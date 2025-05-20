import profileLogo from '../assets/boy.png'
import firmLogo from '../assets/firm.png';
import './css/AboutPage.css'

function AboutPage(){


    return (
        <div className="about-container">
            <div className="paragraph">
                <img src={profileLogo} alt="Profile" />
                <p>
                    Zdravím, jmenuji se Michal. Tato stránka by měla reprezentovat mou diplomovou práci nazvanou
                    <i><b> Tréninkový simulátor pro dispečery s prvky gamifikace a využitím velkých předtrénovaných jazykových modelů</b></i>.
                    V tomto projektu se zaměřuji na vytvoření simulátoru pro dispečery.
                    Doufám, že tato práce pomůže dispečerům připravit se a trénovat své dovednosti v bezpečném prostředí.
                    Tato stránka by však měla být i pro kohokoliv, kdo by se chtěl jednou stát dispečerem.
                    Nakonec může být tato webová stránka vnímána doslovně jako hra pro každého a může být hrána bez ohledu na věk.
                    Můžete dokonce porovnávat své výsledky s ostatními hráči nebo se pokusit překonat své vlastní.
                </p>
            </div>
            <div className="paragraph-right">
                <p>
                    Rád bych poděkoval RNDr. Marku Rychlému, Ph.D. za odborné vedení, podporu a vstřícnost během celé práce.
                    Zároveň vyjadřuji velké díky Marku Řehůlkovi z firmy <i><b> GINA Software</b></i>, který poskytl cenné konzultace
                    i data, a umožnil mi nahlédnout do reálného prostředí operátorů. GINA Software je brněnská technologická
                    společnost založená v roce 2010, která se specializuje na vývoj softwarových
                    řešení pro krizové řízení a koordinaci záchranných složek. Jejich systémy, jako je
                    <i><b> GINA Central</b></i> a <i><b>  GINA GO</b></i>, jsou navrženy tak, aby poskytovaly přehled
                    o situaci v reálném čase, usnadňovaly komunikaci a zvyšovaly efektivitu zásahů.
                    Technologie GINA byly nasazeny při různých krizových situacích, včetně zemětřesení na Haiti,
                    a jsou využívány organizacemi jako je<i><b> Český červený kříž</b></i>.
                </p>
                <img src={firmLogo} alt="Firm Logo" />
            </div>

        </div>
    )
}

export default AboutPage;