package com.example.hackathon_infoed

import News
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.analytics.AnalyticsListener
import com.example.hackathon_infoed.ui.theme.Hackathon_InfoedTheme
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

class show_data : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val title = intent.getStringExtra("title")
        setContent {
            val context = LocalContext.current
            Column(modifier = Modifier
                .fillMaxSize()
                .offset(y = 30.dp)
                .padding(16.dp)) {
                Button(onClick = { finish() }) {
                    Text("Back")
                }
                Text(
                    text = "Description: $title",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Toast.makeText(this@show_data, title, Toast.LENGTH_SHORT).show()
                loAD_video(reactionVideos[title]!!)
                Text(
                    text = "Safety Instructions: ${Safety_instructions[title]}",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                ReactionsAchievedRow(reactions_achieved, reactionDetails)

                /* Button(onClick ={
                     val intent = Intent(context, Noutati::class.java)
                     context.startActivity(intent)
                 }) {
                     Text("View Safety Instructions")
                 }*/
            }
        }
    }
    val reactionDetails: Map<String, Pair<Int, String>> = mapOf(
        "Melting" to Pair(R.drawable.metal_melting, "Definiția Topirii:\n" +
                "Topirea este procesul prin care o substanță solidă se transformă într-un lichid. Acest lucru se întâmplă atunci când substanța absoarbe căldură, făcând ca moleculele sale să capete energie și să vibreze mai intens până când își părăsesc pozițiile fixe.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Punctul de Topire:\n" +
                "\n" +
                "Temperatura la care un solid se transformă într-un lichid se numește punct de topire. La această temperatură, stările solide și lichide ale substanței sunt în echilibru.\n" +
                "Substanțele diferite au puncte de topire diferite, în funcție de structura lor moleculară și de forțele intermoleculare.\n" +
                "Căldură și Energie:\n" +
                "\n" +
                "Topirea este un proces endotermic, ceea ce înseamnă că necesită absorbția de căldură.\n" +
                "Energia absorbită în timpul topirii este utilizată pentru a învinge forțele care țin particulele într-o structură solidă.\n" +
                "Căldura Latentă de Fuziune:\n" +
                "\n" +
                "Cantitatea de căldură necesară pentru a transforma o unitate de masă a unui solid într-un lichid la punctul său de topire se numește căldură latentă de fuziune.\n" +
                "Această căldură nu modifică temperatura, ci schimbă starea substanței.\n" +
                "Exemple de Topire:\n" +
                "\n" +
                "Gheața care se transformă în apă la 0°C\n" +
                "Ciocolata care se topește atunci când este încălzită\n" +
                "Metale precum aurul care se topesc într-un cuptor\n" +
                "Aplicații ale Topirii:\n" +
                "\n" +
                "Topirea este crucială în diverse industrii, cum ar fi metalurgia, procesarea alimentelor și producția de materiale.\n" +
                "Înțelegerea procesului de topire este importantă și în fenomenele naturale, cum ar fi topirea calotelor polare."),
        "Fire expansion" to Pair(R.drawable.fire_exp, "Definiția Expansiunii Focului:\n" +
                "Expansiunea focului se referă la procesul prin care un incendiu se extinde, afectând noi zone și materiale. Acest fenomen este influențat de o serie de factori, cum ar fi materialele combustibile disponibile, condițiile atmosferice și topografia terenului.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Triunghiul Focului:\n" +
                "\n" +
                "Combustibil: Materialele care pot arde, cum ar fi lemnul, hârtia, combustibilii lichizi sau gazoși.\n" +
                "Oxigen: Aerul necesar pentru menținerea combustiei; oxigenul este un element esențial în reacțiile de ardere.\n" +
                "Căldură: Energia necesară pentru a începe și a menține procesul de ardere.\n" +
                "Factori care Influențează Expansiunea Focului:\n" +
                "\n" +
                "Materiale Combustibile: Tipul, cantitatea și distribuția materialelor combustibile influențează cât de rapid se extinde focul.\n" +
                "Vântul: Poate transporta scântei și poate accelera propagarea flăcărilor.\n" +
                "Umiditatea: Nivelul de umiditate din aer și materialele combustibile poate încetini sau accelera arderea.\n" +
                "Topografia: Pantele pot favoriza expansiunea focului în sus, iar vârfurile și crestele pot acționa ca bariere naturale.\n" +
                "Metode de Prevenire și Control al Focului:\n" +
                "\n" +
                "Baraje de Foc: Crearea de zone fără vegetație sau materiale inflamabile pentru a opri extinderea focului.\n" +
                "Focuri Controlate: Utilizarea intenționată a focului pentru a arde combustibilul disponibil într-o manieră controlată, prevenind astfel incendii mari.\n" +
                "Mijloace de Stingere: Utilizarea apei, spumei, chimicalelor sau alte metode pentru a reduce căldura și a stinge focul.\n" +
                "Risc și Siguranță:\n" +
                "\n" +
                "Incendiile pot pune în pericol viețile oamenilor, animalele și proprietățile. Este esențial să existe planuri de evacuare și măsuri de siguranță.\n" +
                "Educația publicului despre prevenirea incendiilor și comportamentul în caz de incendiu este crucială."),
        "Explosion" to Pair(R.drawable.expl, "Definiția Exploziei:\n" +
                "O explozie este o eliberare bruscă și violentă de energie, care rezultă într-o expansiune rapidă de gaze și o creștere bruscă a presiunii și temperaturii. Exploziile pot fi cauzate de reacții chimice, fizice sau nucleare.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Tipuri de Explozii:\n" +
                "\n" +
                "Explozii Chimice: Cauzate de reacții chimice rapide, cum ar fi detonarea explozibililor sau reacția combustibililor cu oxidanți.\n" +
                "Explozii Fizice: Rezultă din eliberarea bruscă a presiunii, cum ar fi explozia unui rezervor de gaz sub presiune.\n" +
                "Explozii Nucleare: Rezultă din reacții nucleare, cum ar fi fisiunea sau fuziunea nucleară, care eliberează o cantitate imensă de energie.\n" +
                "Mecanismul Exploziei:\n" +
                "\n" +
                "Exploziile implică o reacție rapidă de eliberare de energie, care duce la expansiunea bruscă a gazelor.\n" +
                "Această expansiune produce o undă de șoc, care se propagă prin aer sau alte medii și poate cauza daune structurale.\n" +
                "Factori Determinanți:\n" +
                "\n" +
                "Natura Materialului Exploziv: Compoziția chimică și structura materialului influențează viteza și intensitatea exploziei.\n" +
                "Condițiile de Mediu: Presiunea, temperatura și umiditatea pot afecta reactivitatea și comportamentul exploziei.\n" +
                "Confinarea: Exploziile sunt adesea mai puternice când materialele explozive sunt confinante, deoarece presiunea crește semnificativ înainte de a fi eliberată.\n" +
                "Efecte ale Exploziilor:\n" +
                "\n" +
                "Unda de Șoc: Presiunea extrem de ridicată poate provoca daune semnificative la structuri și poate răni sau ucide persoane.\n" +
                "Fragmente: Materialele distruse în timpul exploziei pot deveni fragmente periculoase care se propagă cu viteză mare.\n" +
                "Căldură și Incendiu: Multe explozii generează temperaturi ridicate și pot aprinde materiale inflamabile din jur.\n" +
                "Prevenire și Securitate:\n" +
                "\n" +
                "Măsuri de Siguranță: Include controlul accesului la materiale explozive, monitorizarea condițiilor de depozitare și manipulare corectă.\n" +
                "Detectarea și Evaluarea Riscului: Identificarea potențialelor surse de explozie și evaluarea riscurilor asociate.\n" +
                "Echipament de Protecție: Utilizarea echipamentelor de protecție personală și a sistemelor de protecție structurală pentru a minimiza efectele exploziei."),
        "Mud" to Pair(R.drawable.mudification_with_ash, "Definiția Noroiului:\n" +
                "Noroiul este un material moale, umed și lipicios, format prin amestecul de particule de sol, argilă și apă. Se găsește în mod obișnuit în zonele cu umiditate ridicată, cum ar fi malurile râurilor, fundul lacurilor, mlaștinile și alte suprafețe umede.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Formarea Noroiului:\n" +
                "\n" +
                "Componente: Noroiul este compus din particule mici de sol și minerale (cum ar fi argila și nisipul), combinate cu apă.\n" +
                "Proces de Formare: Se formează în urma proceselor de eroziune, transport și depunere a particulelor de sol, în combinație cu umezeala din precipitații, inundații sau alte surse de apă.\n" +
                "Tipuri de Noroi:\n" +
                "\n" +
                "Noroi Argilos: Conține un procent ridicat de argilă, ceea ce îi conferă o textură fină și lipicioasă.\n" +
                "Noroi Siltos: Conține particule de silt, care sunt mai mari decât cele de argilă, dar mai mici decât nisipul, având o textură mai puțin lipicioasă.\n" +
                "Noroi Nisipos: Conține o proporție mai mare de particule de nisip, fiind mai grosier și mai puțin lipicios.\n" +
                "Rolul Noroiului în Natură:\n" +
                "\n" +
                "Habitat: Oferă un habitat important pentru numeroase specii de plante, animale și microorganisme. Este esențial pentru ecosistemele mlaștinilor și zonelor umede.\n" +
                "Filtrare: Noroiul poate filtra și purifica apa prin adsorbția contaminanților și a substanțelor nutritive.\n" +
                "Retenția Apei: Noroiul reține apa și o eliberează treptat, contribuind la reglarea nivelului apei în sol și la prevenirea eroziunii.\n" +
                "Utilizări ale Noroiului:\n" +
                "\n" +
                "Agricultură: Solurile bogate în noroi sunt adesea fertile și potrivite pentru agricultură datorită conținutului ridicat de substanțe nutritive.\n" +
                "Construcții: În unele culturi, noroiul este utilizat în construcția de case și structuri, fie ca material principal, fie în amestec cu alte materiale (de exemplu, cărămizi de noroi sau chirpici).\n" +
                "Tratamente Termale: Noroiul mineral este utilizat în tratamentele spa și balneare pentru proprietățile sale terapeutice.\n" +
                "Impactul Noroiului:\n" +
                "\n" +
                "Pozitiv: Contribuie la fertilitatea solului, susține biodiversitatea și ajută la reglarea ciclului apei.\n" +
                "Negativ: În exces, noroiul poate cauza probleme de accesibilitate, poate acoperi culturi agricole și poate afecta infrastructura."),
        "Rapid cooling of lava" to Pair(R.drawable.rpd_cool_lava, "Definiția Lavei:\n" +
                "Lava este roca topită care erupe la suprafața Pământului în timpul unei erupții vulcanice. Când lava ajunge la suprafață, aceasta se răcește și se solidifică, formând diverse tipuri de roci vulcanice.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Răcirea Lavei:\n" +
                "\n" +
                "Răcire Rapidă: Procesul prin care lava se solidifică rapid atunci când intră în contact cu aerul, apa sau alte suprafețe reci. Răcirea rapidă se întâmplă adesea la marginile fluxului de lavă sau când lava intră în contact cu apa (de exemplu, în oceane).\n" +
                "Formarea Rocilor Vulcanice:\n" +
                "\n" +
                "Textură Sticloasă: Răcirea rapidă nu oferă suficient timp pentru formarea cristalelor mari, astfel că rocile formate au o textură fină sau chiar sticloasă. Un exemplu comun este obsidianul, o rocă vulcanică neagră și lucioasă.\n" +
                "Pietre Ponce: O altă rocă vulcanică formată prin răcirea rapidă a lavei este piatra ponce, care are o textură poroasă și este atât de ușoară încât poate pluti pe apă. Aceasta se formează din lavă bogată în gaze care se solidifică rapid, păstrând bule de gaz în interior.\n" +
                "Procese Asociate cu Răcirea Rapidă:\n" +
                "\n" +
                "Fragmentare Explozivă: Atunci când lava bogată în gaze se răcește rapid, poate avea loc o fragmentare explozivă, creând tefra sau cenușă vulcanică.\n" +
                "Coloane de Abur: Lava care se răcește rapid în apă produce adesea coloane de abur, rezultând din vaporizarea apei în contact cu materialul fierbinte.\n" +
                "Impactul Răcirii Rapide:\n" +
                "\n" +
                "Formarea Insulelor: În unele cazuri, răcirea rapidă a lavei care erupe sub apă poate duce la formarea de insule noi, ca în cazul insulelor vulcanice din oceane.\n" +
                "Structuri de Lave: Lava cu răcire rapidă poate forma diverse structuri interesante, cum ar fi lava pillow (lava în formă de perne) sau coloane de lava sticloasă.\n" +
                "Exemple și Locații:\n" +
                "\n" +
                "Lava Obsidianică: Poate fi găsită în multe regiuni vulcanice din lume, cum ar fi Parcul Național Yellowstone din Statele Unite.\n" +
                "Insula Surtsey: Formată în urma unei erupții subacvatice lângă Islanda, este un exemplu de insulă creată prin răcirea rapidă a lavei."),
        "Freezing" to Pair(R.drawable.frz, "Definiția Înghețului:\n" +
                "Înghețul este fenomenul prin care apa sau alte lichide trec din stare lichidă în stare solidă (gheață) ca urmare a scăderii temperaturii sub punctul de îngheț. În cazul apei pure, acest punct este la 0°C (32°F).\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Procesul de Îngheț:\n" +
                "\n" +
                "Pierderea Energiei Termice: Înghețul are loc atunci când un lichid pierde suficientă energie termică, ceea ce determină încetinirea mișcării moleculelor până când acestea se aranjează într-o structură cristalină solidă.\n" +
                "Cristalizare: În timpul înghețului, moleculele de apă se aliniază într-o formă ordonată, formând structuri cristaline care constituie gheața.\n" +
                "Punctul de Îngheț:\n" +
                "\n" +
                "Apa Pură: Are un punct de îngheț de 0°C la presiunea atmosferică normală.\n" +
                "Soluții și Amestecuri: Prezența sărurilor sau a altor substanțe dizolvate poate scădea punctul de îngheț al apei, fenomen cunoscut sub numele de depresie a punctului de îngheț. De exemplu, apa sărată îngheață la temperaturi mai scăzute decât apa pură.\n" +
                "Tipuri de Îngheț:\n" +
                "\n" +
                "Îngheț la Sol: Se formează când temperatura la suprafața solului scade sub punctul de îngheț, dar aerul de la nivelul solului nu scade sub zero grade.\n" +
                "Îngheț Aerian: Se produce atunci când temperatura aerului scade sub 0°C, afectând întreaga coloană de aer de la sol.\n" +
                "Efectele Înghețului:\n" +
                "\n" +
                "Efecte asupra Plantelor: Înghețul poate deteriora țesuturile plantelor, conducând la distrugerea culturilor și a vegetației. Fenomenul de îngheț târziu este deosebit de periculos pentru agricultură, deoarece apare după ce plantele au început să crească.\n" +
                "Efecte asupra Clădirilor și Infrastructurii: Înghețul poate cauza fisuri în structurile de beton, spargerea țevilor de apă și deteriorarea drumurilor.\n" +
                "Formarea Gheții: Înghețul pe suprafețe precum drumurile sau trotuarele poate crea condiții periculoase pentru trafic și pietoni.\n" +
                "Protecția Împotriva Înghețului:\n" +
                "\n" +
                "Acoperirea Plantelor: Folosirea de pături, mulci sau alte materiale pentru a proteja plantele de îngheț.\n" +
                "Utilizarea Antigelului: În sisteme precum cele de răcire a automobilelor, antigelul este utilizat pentru a preveni înghețul lichidului de răcire.\n" +
                "Salinizare: Aplicarea de sare pe drumuri pentru a reduce punctul de îngheț al apei și a preveni formarea gheții."),
        "Making water" to Pair(R.drawable.mkg_water, "Explosion Risk: Handle hydrogen with care as it is highly flammable.\nProper Ventilation: Ensure adequate ventilation to avoid gas buildup."),
        "combustion" to Pair(R.drawable.combustion, "Definiția Combustiei:\n" +
                "Combustia este o reacție chimică exotermă care implică oxidarea rapidă a unui combustibil, rezultând eliberarea de energie sub formă de căldură și lumină. Este un proces cheie în multe aplicații industriale și zilnice, de la arderea combustibililor fosili la metabolismul în organisme.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Reacția Chimică a Combustiei:\n" +
                "\n" +
                "Combustibil: Substanța care arde în procesul de combustie, de obicei conținând elemente precum carbon (C) și hidrogen (H).\n" +
                "Oxidant: Substanța care furnizează oxigenul necesar pentru combustie. În majoritatea cazurilor, oxidantul este oxigenul din aer.\n" +
                "Produse de Combustie: Substanțele rezultate din reacție, de obicei dioxid de carbon (CO₂), apă (H₂O) și alte gaze sau particule.\n" +
                "Tipuri de Combustie:\n" +
                "\n" +
                "Combustie Completă: Are loc atunci când combustibilul este ars complet în prezența unui exces de oxigen, rezultând în principal dioxid de carbon și apă. Exemplu: Arderea metanului \n" +
                "CH\n" +
                "4\n" +
                "+\n" +
                "2\n" +
                "O\n" +
                "2\n" +
                "→\n" +
                "CO\n" +
                "2\n" +
                "+\n" +
                "2\n" +
                "H\n" +
                "2\n" +
                "O\n" +
                "CH \n" +
                "4\n" +
                "\u200B\n" +
                " +2O \n" +
                "2\n" +
                "\u200B\n" +
                " →CO \n" +
                "2\n" +
                "\u200B\n" +
                " +2H \n" +
                "2\n" +
                "\u200B\n" +
                " O.\n" +
                "Combustie Incompletă: Apare atunci când nu există suficient oxigen, ceea ce duce la producerea de monoxid de carbon (CO), particule de carbon (funingine) și alți compuși. Exemplu: \n" +
                "2\n" +
                "CH\n" +
                "4\n" +
                "+\n" +
                "3\n" +
                "O\n" +
                "2\n" +
                "→\n" +
                "2\n" +
                "CO\n" +
                "+\n" +
                "4\n" +
                "H\n" +
                "2\n" +
                "O\n" +
                "2CH \n" +
                "4\n" +
                "\u200B\n" +
                " +3O \n" +
                "2\n" +
                "\u200B\n" +
                " →2CO+4H \n" +
                "2\n" +
                "\u200B\n" +
                " O.\n" +
                "Combustie Spontană: Are loc fără o sursă de aprindere externă, atunci când materialul combustibil se încălzește intern până la punctul de aprindere.\n" +
                "Condiții Necesare pentru Combustie (Triunghiul Focului):\n" +
                "\n" +
                "Combustibil: Material care poate arde.\n" +
                "Oxigen: Gaz necesar pentru susținerea arderii.\n" +
                "Căldură: Energia necesară pentru a iniția și a susține reacția de combustie.\n" +
                "Energia Eliberată:\n" +
                "\n" +
                "Combustia este o reacție exotermă, ceea ce înseamnă că eliberează energie sub formă de căldură și, adesea, lumină. Această energie poate fi utilizată pentru încălzire, iluminare, generare de energie mecanică (în motoare), etc.\n" +
                "Aplicații și Importanță:\n" +
                "\n" +
                "Industrie și Transport: Combustia combustibililor fosili este esențială pentru producerea de energie electrică, încălzirea clădirilor și alimentarea vehiculelor.\n" +
                "Procese Chimice: Folosirea flăcării pentru sudură, tăiere și alte procese industriale.\n" +
                "Siguranță și Control: Prevenirea și controlul incendiilor, detectarea gazelor inflamabile, etc.\n" +
                "Impactul Asupra Mediului:\n" +
                "\n" +
                "Poluarea Aerului: Combustia incompletă poate elibera poluanți cum ar fi monoxidul de carbon, particulele fine și compușii organici volatili.\n" +
                "Efectul de Seră: Arderea combustibililor fosili contribuie la emisia de dioxid de carbon, un gaz cu efect de seră care contribuie la schimbările climatice."),
        "Calcium oxidation" to Pair(R.drawable.ca_oxd, "Definiția Oxidării Calciului:\n" +
                "Oxidarea calciului se referă la reacția chimică în care calciul (Ca) reacționează cu oxigenul (O₂) din atmosferă pentru a forma oxid de calciu (CaO). Această reacție este un exemplu de oxidare a unui metal.\n" +
                "\n" +
                "Reacția Chimică:\n" +
                "2\n" +
                "Ca\n" +
                "+\n" +
                "O\n" +
                "2\n" +
                "→\n" +
                "2\n" +
                "CaO\n" +
                "2Ca+O \n" +
                "2\n" +
                "\u200B\n" +
                " →2CaO\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Oxidarea Calciului:\n" +
                "\n" +
                "Calciu (Ca): Este un metal alcalino-pământos, moale și de culoare argintie, care se oxidează ușor.\n" +
                "Oxid de Calciu (CaO): Este un compus ionic format prin reacția dintre calciu și oxigen. Este o substanță albă, solidă, cu o structură cristalină.\n" +
                "Energia de Reacție: Reacția de oxidare a calciului este exotermă, adică eliberează căldură.\n" +
                "Procesul de Oxidare:\n" +
                "\n" +
                "Răcire și Reacție: La temperaturi normale, calciul reacționează rapid cu oxigenul, formând oxidul de calciu. Reacția se accelerează atunci când calciul este încălzit.\n" +
                "Formarea Oxidului: Oxidul de calciu se formează atunci când atomii de calciu pierd electroni și se combină cu oxigenul, rezultând într-un compus ionic.\n" +
                "Proprietăți ale Oxidului de Calciu:\n" +
                "\n" +
                "Aspect: Este un solid alb, pulverulent, cunoscut și sub numele de var nestins.\n" +
                "Solubilitate: Este puțin solubil în apă. Când se adaugă apă, oxidele de calciu se transformă în hidroxid de calciu (Ca(OH)₂), un proces exothermic.\n" +
                "Reactivitate: Este utilizat în diferite aplicații industriale și chimice datorită proprietăților sale de bazicitate.\n" +
                "Aplicații ale Oxidului de Calciu:\n" +
                "\n" +
                "Industria Construției: Folosit ca ingredient în fabricarea cimentului. Calciul se combină cu alte componente pentru a produce un ciment durabil.\n" +
                "Tratamentul Apelor: Utilizat pentru neutralizarea acizilor în tratamentele de apă și pentru reducerea durității apei.\n" +
                "Agricultură: Aplicat pe soluri pentru a corecta aciditatea și a îmbunătăți condițiile de creștere a plantelor.\n" +
                "Impactul și Securitatea:\n" +
                "\n" +
                "Siguranță: Oxidul de calciu poate fi iritant pentru piele și ochi, și poate provoca arsuri dacă intră în contact cu apa.\n" +
                "Manipulare: Este important să se utilizeze echipament de protecție adecvat și să se evite inhalarea prafului sau contactul direct cu substanța."),
        "Obsidian" to Pair(R.drawable.obsidian, "Definiția Obsidianului:\n" +
                "Obsidianul este o rocă vulcanică sticloasă care se formează din lava răcită rapid, având o textură sticloasă și lucioasă. Este cunoscut pentru culoarea sa neagră strălucitoare, dar poate varia în nuanțe de la brun până la verde sau gri, în funcție de impuritățile conținute.\n" +
                "\n" +
                "Formarea și Caracteristicile Obsidianului:\n" +
                "\n" +
                "Formarea Obsidianului:\n" +
                "\n" +
                "Lava Răcită Rapid: Obsidianul se formează când lava bogată în silice se răcește extrem de rapid, în general atunci când intră în contact cu apă sau cu suprafețe reci. Răcirea rapidă împiedică cristalizarea mineralelor, rezultând într-un material amorf, adică fără structură cristalină.\n" +
                "Structură Sticloasă: Lipsa timpului necesar pentru cristalizare duce la formarea unei mase vitreene care este transparentă sau translucidă și care are o textură similară cu sticla.\n" +
                "Proprietăți:\n" +
                "\n" +
                "Aspect: Este de obicei de culoare neagră lucioasă, dar poate avea și nuanțe de brun, verde, sau chiar roșu, în funcție de impuritățile și tipurile de minerale prezente.\n" +
                "Duritate: Este destul de dur și fragil, având o duritate de aproximativ 5-6 pe scara Mohs. Se rupe în fragmente ascuțite, ceea ce îl face util în unele aplicații.\n" +
                "Fragilitate: Este foarte fragil și poate fi ușor spart sau crăpat. De aceea, este mai potrivit pentru aplicatii care nu implică impacturi fizice mari.\n" +
                "Utilizări Ale Obsidianului:\n" +
                "\n" +
                "Instrumente Preistorice: A fost utilizat de civilizațiile antice pentru fabricarea de unelte și arme, datorită ascuțimii marginilor sale atunci când se rupe. Exemple includ săgeți și lame de cuțit."),
        "Lava cooling" to Pair(R.drawable.lava_cooling, "\n" +
                "Rezumat Lecție: Răcirea Lavei\n" +
                "Definiția Răcirii Lavei:\n" +
                "Răcirea lavei se referă la procesul prin care lava, magma topită care erupe la suprafață, își reduce temperatura și se solidifică, formând diferite tipuri de roci vulcanice. Acest proces este esențial pentru modelarea peisajelor vulcanice și pentru formarea crustei terestre.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Procesul de Răcire:\n" +
                "\n" +
                "Răcirea Rapidă: Lava care intră în contact cu aerul sau apa se răcește rapid, ceea ce împiedică cristalizarea completă a mineralelor. Acest proces produce roci vulcanice cu o textură sticloasă, cum ar fi obsidianul.\n" +
                "Răcirea Lentă: Lava care se răcește treptat, de obicei în interiorul părților adânci ale pământului sau în zonele izolate de aer, permite cristalizarea mineralelor și duce la formarea unor roci cu o textură granulară, cum ar fi bazaltul sau andezitul.\n" +
                "Tipuri de Lava și Răcirea Lor:\n" +
                "\n" +
                "Lava Aşchie (Pahoehoe): Are o textură netedă și lucioasă, și se formează prin răcirea lentă a lavei, care permite formarea unor structuri de suprafață ondulate sau „de valuri”.\n" +
                "Lava Așchiușă (ʻAʻā): Este mai fragmentată și aspră, cu o textură sticloasă sau rugoasă. Se formează prin răcirea rapidă și izbucnirea de gaz a lavei, care duce la formarea de bucăți fragmentate și corozive.\n" +
                "Forme Geologice Create de Răcirea Lavei:\n" +
                "\n" +
                "Fluxuri de Lava: Formațiuni de lavă care se extind de la vulcan și se răcesc într-o formă variabilă, în funcție de tipul de lava și condițiile de mediu.\n" +
                "Mese de Lava: Se formează atunci când lava curge de-a lungul unei regiuni și se răcește în straturi suprapuse, creând forme plate și extinse.\n" +
                "Colonii de Lava: Structuri formate prin răcirea lava în coloane regulate, adesea vizibile în formațiuni geologice precum bazaltul de coloană.\n" +
                "Efectele Răcirii Lavei:\n" +
                "\n" +
                "Formarea Rocilor Vulcanice: Răcirea lava duce la formarea diferitelor tipuri de roci, fiecare cu caracteristici unice în funcție de viteza de răcire și compoziția chimică a lavei.\n" +
                "Modelarea Peisajului: Lava solidificată ajută la formarea peisajelor vulcanice, incluzând craterele, conurile vulcanice și câmpiile de lavă.\n" +
                "Impactul Asupra Ecosistemelor: Lava care se răcește rapid poate crea habitate unice pentru plante și animale, în timp ce lava care se răcește lent poate modifica solul și peisajul pe termen lung.\n" +
                "Factori care Influențează Răcirea:\n" +
                "\n" +
                "Temperatura Lava: Lava fierbinte se răcește mai lent comparativ cu lava mai rece.\n" +
                "Compoziția Lava: Lava bogată în silice se răcește mai repede și poate forma roci mai sticloase.\n" +
                "Medii de Răcire: Lava care intră în contact cu apă se răcește rapid și poate forma structuri unice, cum ar fi lavele pillow (în formă de pernă)."),
        "Fire melting ice" to Pair(R.drawable.brn_wood, "Definiția Procesului:\n" +
                "Gheața poate contribui la stingerii focului prin două mecanisme principale: scăderea temperaturii și reducerea cantității de oxigen disponibil. Aceasta se întâmplă în principal prin două modalități: directă și indirectă.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Scăderea Temperaturii:\n" +
                "\n" +
                "Absorbția Căldurii: Când gheața se topește, absoarbe o cantitate semnificativă de căldură. Procesul de topire al gheții (de la solid la lichid) necesită o cantitate mare de energie, cunoscută sub numele de căldura de topire. Aceasta scade temperatura în jurul focului, ceea ce ajută la răcirea și eventual stingerea flăcărilor.\n" +
                "Efectul de Răcire: Pe măsură ce gheața se transformă în apă, temperatura mediului înconjurător scade, iar acest lucru poate reduce eficiența reacțiilor de combustie, având ca rezultat o scădere a intensității focului.\n" +
                "Reducerea Cantității de Oxigen:\n" +
                "\n" +
                "Acoperirea Focului: Când gheața se topește și se transformă în apă, ea poate acoperi focul și astfel reduce expunerea acestuia la oxigen. Focul are nevoie de oxigen pentru a se menține, iar acoperirea cu apă poate reduce cantitatea de oxigen disponibilă, facilitând stingerea focului.\n" +
                "Formarea Vaporilor: Apa fierbinte se evaporă rapid în vaporii de apă. Vaporii de apă pot ajuta la diluarea oxigenului din apropierea flăcărilor și la micșorarea intensității focului.\n" +
                "Procese Fizice și Termodinamice:\n" +
                "\n" +
                "Căldura de Vaporizare: Dacă apa de pe gheață se evaporă, acest proces necesită și mai multă energie termică, ceea ce contribuie la răcirea suplimentară a focului.\n" +
                "Răcirea și Dilutarea: Vaporii de apă și lichidul (apă) răcesc și diluează zonele în care se află focul, reducând eficiența reacțiilor de combustie și scăzând temperatura necesară pentru menținerea focului.\n" +
                "Utilizarea Practică:\n" +
                "\n" +
                "Stingerea Focurilor: În situații de urgență, gheața și apa sunt adesea utilizate pentru a stinge focurile, mai ales în incendii de mici dimensiuni sau în situații unde resursele sunt limitate.\n" +
                "Focuri de Bucătărie: În unele situații, gheața poate fi utilizată pentru a stinge mici incendii de bucătărie sau focuri de ulei, însă trebuie utilizată cu precauție pentru a evita riscurile asociate cu vaporizarea rapidă.\n" +
                "Limitări și Riscuri:\n" +
                "\n" +
                "Răspuns la Tipul de Foc: Gheața poate fi ineficientă în cazul unor incendii mari sau incendii de substanțe inflamabile, unde apa nu este recomandată sau unde focul poate continua sub apă.\n" +
                "Pericolul Vaporizării: În cazul focurilor mari, folosirea gheții poate crea vapori de apă în cantități mari, care pot duce la expansiunea rapidă și la potențiale explozii."),
        "Thermal Decomposition" to Pair(R.drawable.fire_exp, "Definiția Descompunerii Termice:\n" +
                "Descompunerea termică este un proces chimic prin care o substanță se descompune în două sau mai multe substanțe mai simple atunci când este expusă la căldură. Această reacție este adesea utilizată pentru a studia stabilitatea termică a compușilor și pentru a obține substanțe chimice pure.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Procesul de Descompunere Termică:\n" +
                "\n" +
                "Încălzire: Atunci când un compus chimic este încălzit, energia termică poate provoca ruperea legăturilor chimice din structura sa moleculară.\n" +
                "Descompunere: Compusul se descompune în componente mai simple, care pot fi gaze, lichide sau solide, în funcție de natura substanței originale și de condițiile de descompunere.\n" +
                "Tipuri de Descompunere Termică:\n" +
                "\n" +
                "Descompunere în Gaze: Unele substanțe se descompun în gaze atunci când sunt încălzite. De exemplu, nitratul de sodiu (NaNO₃) se descompune în nitrat de sodiu (NaNO₂) și oxigen (O₂) în timpul încălzirii.\n" +
                "Descompunere în Solid și Gaz: Alte substanțe pot produce un solid și un gaz. De exemplu, carbonatul de calciu (CaCO₃) se descompune în oxid de calciu (CaO) și dioxid de carbon (CO₂) când este încălzit.\n" +
                "Exemple de Descompunere Termică:\n" +
                "\n" +
                "Descompunerea Carbonatului de Calciu:\n" +
                "CaCO\n" +
                "3\n" +
                "→\n" +
                "CaO\n" +
                "+\n" +
                "CO\n" +
                "2\n" +
                "CaCO \n" +
                "3\n" +
                "\u200B\n" +
                " →CaO+CO \n" +
                "2\n" +
                "\u200B\n" +
                " \n" +
                "Carbonatul de calciu se descompune în oxid de calciu și dioxid de carbon la temperaturi ridicate. Acest proces este important în fabricarea cimentului și a varului.\n" +
                "Descompunerea Nitraților:\n" +
                "2\n" +
                "KNO\n" +
                "3\n" +
                "→\n" +
                "2\n" +
                "KNO\n" +
                "2\n" +
                "+\n" +
                "O\n" +
                "2\n" +
                "2KNO \n" +
                "3\n" +
                "\u200B\n" +
                " →2KNO \n" +
                "2\n" +
                "\u200B\n" +
                " +O \n" +
                "2\n" +
                "\u200B\n" +
                " \n" +
                "Nitrații, cum ar fi nitratul de potasiu (KNO₃), se descompun în nitriți și oxigen, fiind utilizat în industria chimică și în producția de explozibili.\n" +
                "Factori Care Influentează Descompunerea Termică:\n" +
                "\n" +
                "Temperatura: Descompunerea termică are loc la o temperatură specifică pentru fiecare substanță, cunoscută sub numele de temperatura de descompunere. Fiecare substanță are o temperatură de descompunere caracteristică.\n" +
                "Presiunea: Presiunea poate influența procesul de descompunere, în special pentru compușii care se descompun în gaze. Într-un mediu de presiune diferită, produsele și ratele de descompunere pot varia.\n" +
                "Catalizatori: Unele reacții de descompunere pot fi accelerate de catalizatori, care sunt substanțe care accelerează reacția fără a fi consumate în proces.\n" +
                "Aplicații Practice:\n" +
                "\n" +
                "Industria Chimică: Descompunerea termică este folosită pentru a produce substanțe chimice esențiale și pentru a elimina impuritățile.\n" +
                "Analiza Materialelor: Este utilizată în analiza termogravimetrică pentru a studia stabilitatea și compoziția materialelor.\n" +
                "Reciclarea: Descompunerea termică este folosită în reciclarea materialelor, cum ar fi descompunerea termică a plasticului pentru a produce substanțe reutilizabile.\n" +
                "Impactul și Securitatea:\n" +
                "\n" +
                "Siguranță: Descompunerea termică poate elibera gaze toxice sau inflamabile. Este important să se utilizeze echipament de protecție și să se lucreze într-un spațiu bine ventilat.\n" +
                "Controlul Temperaturii: Monitorizarea atentă a temperaturii este esențială pentru a preveni reacții necontrolate și pentru a obține produse pure."),
        "Steam" to Pair(R.drawable.steam, "Definiția Aburilor:\n" +
                "Aburii sunt vapori de apă care se formează atunci când apa lichidă este încălzită la temperaturi ridicate și se evaporă. Aburul este o formă de apă în stare de vapori, care este invizibilă atunci când se află într-o stare de vapor de înaltă temperatură și presiune, dar devine vizibil când se condensează și formează mici picături de apă.\n" +
                "\n" +
                "Concepte Cheie:\n" +
                "\n" +
                "Formarea Aburilor:\n" +
                "\n" +
                "Evaporare: Aburii se formează atunci când apa lichidă este încălzită la temperatura de evaporare, care este de 100°C (212°F) la presiunea atmosferică normală. Acest proces implică transformarea apei lichide în vapori.\n" +
                "Condensare: Când aburii se răcesc, ei se transformă din nou în apă lichidă. Acest proces apare adesea sub formă de picături de apă vizibile pe feronerie, oglinzi sau în atmosferă, cum ar fi în nori sau ceață.\n" +
                "Proprietăți ale Aburilor:\n" +
                "\n" +
                "Temperatură și Presiune: Aburii au o temperatură și o presiune mai mari comparativ cu apa lichidă. Într-o stare de vapori, apa poate avea o temperatură mult mai mare decât 100°C dacă este menținută la presiuni ridicate.\n" +
                "Capacitatea de a Transporta Energie: Aburii pot transporta o cantitate semnificativă de energie termică. Această proprietate este utilizată în diverse aplicații industriale și energetice.\n" +
                "Utilizări Ale Aburilor:\n" +
                "\n" +
                "Industria Energetică: Aburul este utilizat în centralele electrice pentru a genera energie electrică prin mișcarea turbinelor. Aburul sub presiune ridicată este utilizat pentru a roti turbinele, care, la rândul lor, generează electricitate.\n" +
                "Gătit: Aburul este folosit în gătit, cum ar fi la prepararea legumelor sau a altor alimente la abur, datorită modului eficient în care pătrunde în alimente și le gătește uniform.\n" +
                "Încălzire: În sistemele de încălzire centrală, aburul este utilizat pentru a distribui căldura în întreaga clădire. Sistemele de încălzire cu abur sunt frecvent utilizate în clădiri vechi și în anumite procese industriale.\n" +
                "Termodinamică și Aburi:\n" +
                "\n" +
                "Căldura Latentă: Aburul are o căldură latentă de vaporizare, adică energia necesară pentru a transforma apa lichidă în abur fără a schimba temperatura. Această proprietate este esențială în procesele de evaporare și condensare.\n" +
                "Proprietăți de Presiune: Aburii pot exista la presiuni diferite, iar relația dintre temperatura și presiunea aburului este descrisă de legea lui Clausius-Clapeyron. Aburul la presiuni mari poate fi utilizat în procese industriale și în centrale electrice.\n" +
                "Impactul și Considerații:\n" +
                "\n" +
                "Siguranță: Manipularea aburului la temperaturi și presiuni ridicate poate fi periculoasă, deoarece poate provoca arsuri severe. Este important să se utilizeze echipament de protecție și să se respecte măsurile de siguranță.\n" +
                "Eficiență Energetică: Utilizarea aburului în procesele industriale și energetice necesită gestionarea atentă a eficienței energetice pentru a maximiza producția și a reduce pierderile de căldură."),
    )

}
@Composable
fun loAD_video(res_id: Int){
    VideoPlayer(
        mediaItems = listOf(
            VideoPlayerMediaItem.RawResourceMediaItem(
                resourceId = res_id,
            ),


            ),
        handleLifecycle = true,
        autoPlay = true,
        usePlayerController = true,
        enablePip = true,
        handleAudioFocus = true,
        controllerConfig = VideoPlayerControllerConfig(
            showSpeedAndPitchOverlay = false,
            showSubtitleButton = false,
            showCurrentTimeAndTotalTime = true,
            showBufferingProgress = false,
            showForwardIncrementButton = true,
            showBackwardIncrementButton = true,
            showBackTrackButton = true,
            showNextTrackButton = true,
            showRepeatModeButton = true,
            controllerShowTimeMilliSeconds = 5_000,
            controllerAutoShow = true,
            showFullScreenButton = true,
        ),
        volume = 0.5f,  // volume 0.0f to 1.0f
        repeatMode = RepeatMode.NONE,       // or RepeatMode.ALL, RepeatMode.ONE
        onCurrentTimeChanged = { // long type, current player time (millisec)
            Log.e("CurrentTime", it.toString())
        },
        playerInstance = { // ExoPlayer instance (Experimental)
            addAnalyticsListener(
                object : AnalyticsListener {
                    // player logger
                }
            )
        },
        modifier = Modifier
            .requiredHeight(300.dp)
            .fillMaxWidth()
        ,
    )
}
@Composable
fun ReactionsAchievedRow(reactions: List<String>, reactionDetails: Map<String, Pair<Int, String>>) {
    val context = LocalContext.current
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(reactions) { reaction ->
            Log.d("kilo", reaction)
            val details = reactionDetails[reaction]
            if (details != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = details.first),
                        contentDescription = reaction,
                        modifier = Modifier.size(100.dp)
                            .clickable {
                                val intent = Intent(context, Noutati::class.java)
                                intent.putExtra("title", reaction)
                                intent.putExtra("description", details.second)
                                intent.putExtra("photo_path", details.first)
                                context.startActivity(intent)
                            }
                    )
                    Text(
                        text = reaction,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
