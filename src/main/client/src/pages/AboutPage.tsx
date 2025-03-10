import profileLogo from '../assets/boy.png'
import firmLogo from '../assets/firm.png';
import './css/AboutPage.css'

function AboutPage(){


    return (
        <div className="about-container">
            <div className="paragraph">
                <img src={profileLogo} alt="Profile" />
                <p>
                    Hello, my name is Michael. This page should represent my final thesis called
                    <i><b> A Training Simulator for Dispatchers with Gamification and the use of Large Pre-trained Language Models</b></i>.
                    In this project I focus on creating a simulator for dispatchers.
                    This work hopefully help dispatchers to prepare and train their skills in a safe environment.
                    But this page should also be for anyone, who would like to became dispatcher some day.
                    Eventually this website can be even taken as a game literally for everyone and can be played no matter the age.
                    You can even compare your results with other players or try to beat your own.
                </p>
            </div>
            <div className="paragraph-right">
                <p>
                    I'm incredibly grateful to<i><b> GINA Software</b></i>, a leading firm specializing in developing advanced software
                    solutions focused on location tracking, security management, and emergency response systems.
                    Their expertise has significantly enriched my work by providing critical data and
                    valuable consultations, enabling me to achieve more accurate simulation and informed decision-making.
                    I sincerely thank<i><b> Marek Řehůlka</b></i> for support, professionalism, and help, which have been instrumental
                    for the creation of this project.
                </p>
                <img src={firmLogo} alt="Firm Logo" />
            </div>

        </div>
    )
}

export default AboutPage;