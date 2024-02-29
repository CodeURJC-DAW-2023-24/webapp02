package com.example.candread.services;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.example.candread.model.Element;
import com.example.candread.model.Review;
import com.example.candread.model.User;
import com.example.candread.model.Element.Countries;
import com.example.candread.model.Element.Genres;
import com.example.candread.model.Element.Seasons;
import com.example.candread.model.Element.States;
import com.example.candread.model.Element.Types;
import com.example.candread.repositories.ElementRepository;
import com.example.candread.repositories.ReviewRepository;
import com.example.candread.repositories.UserRepository;


@Service
public class ElementService {
    
    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    //@PostConstruct
    public void insertElement() throws IOException, SerialException, SQLException{

        //String imagen2 = new String("static/Images/Alas_Sangre.jpg");

        List<String> generosEjemplo1 = new ArrayList<>();
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.ROMANCE.name());

        //Getting the info for the imageFile attribute
        //URL urlImg = new URL("https://m.media-amazon.com/images/I/91OI4F8Fa7L._AC_UF894,1000_QL80_.jpg");
        Resource resource = new ClassPathResource("static/Images/Alas_Sangre.jpg");
        //InputStream inputStream = urlImg.openStream(resource);
        InputStream inputStream = resource.getInputStream();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] imageBytes = outputStream.toByteArray();
        Blob blobi = new SerialBlob(imageBytes);

        // ClassPathResource imgFile = new ClassPathResource("static/img/Alas_Sangre.jpg");
		// byte[] photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		// Blob blobi = new SerialBlob(photoBytes); 

        
        //CONSTRUCTURES DE DATOS BASE EN BASE DE DATOS:
        Element elementoTest1 = new Element("Alas de Sangre", "Vuela... o muere. El nuevo fenómeno de fantasía juvenil del que todo el mundo habla.",
        "Rebecca Yarros", Types.LIBRO.name(), Seasons.OTOÑO.name(), States.COMPLETO.name(), 
        Countries.ESTADOS_UNIDOS.name(), generosEjemplo1);

        elementoTest1.setImageFile(blobi);
        elementRepository.save(elementoTest1);

        Review reviewTest1 = new Review("Viva el romantasy", 5);
        reviewTest1.setElementLinked(elementoTest1);

        Optional<User> userPrueba1 = userRepository.findById((long) 2);
        User antonio = userPrueba1.orElseThrow();
        reviewTest1.setUserLinked(antonio);

        reviewRepository.save(reviewTest1);

        generosEjemplo1.clear();
        //reviewsEjemplo.clear();
        generosEjemplo1.add(Genres.AVENTURA.name());
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest2 = new Element("One Piece", 
        "La historia de un chaval buscando ser el rey de los piratas", 
        "Eiichiro Oda", Types.LIBRO.name(), Seasons.VERANO.name(), 
        States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1);
        elementoTest2.setImageFile(blobi);
        elementRepository.save(elementoTest2);

        Review reviewTest3 = new Review("La tripulación es súper animada y la historia parece no acabar nunca, chopper es monisimo uwu", 4);
        reviewTest3.setElementLinked(elementoTest2);
        reviewRepository.save(reviewTest3);
        Review reviewTest2 = new Review("BUENARDO EL MANGA PAPU", 5);
        reviewTest2.setElementLinked(elementoTest2);
        reviewRepository.save(reviewTest2);
        

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.AVENTURA.name());

        Element elementoTest3 = new Element("Undead Unluck", 
        "La historia de la búsqueda de la mayor muerte entre un no-muerto y una gafe.", 
        "Yoshifumi Tozuka", Types.LIBRO.name(), Seasons.PRIMAVERA.name(), 
        States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1);
        elementRepository.save(elementoTest3);
        

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.NOVELA.name());
        generosEjemplo1.add(Genres.MISTERIO.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest4 = new Element("Marina", 
        "La trama se desarrolla en la segunda mitad del siglo xx y narra dos historias paralelas. La principal es la historia de Óscar y Marina, enternecedora y emotiva. Después, se centra en la enigmática vida y empresa de Mijail Kolvenik, un genio en la creación de artículos de ortopedia y prótesis médicas, que motivado por la locura de superar a la muerte y los errores de las deformaciones humanas termina convirtiéndose en una horrible bestia", 
        "Carlos Luis Zafon", Types.LIBRO.name(), Seasons.INVIERNO.name(), 
        States.COMPLETO.name(), Countries.ESPAÑA.name(), generosEjemplo1);
        elementRepository.save(elementoTest4);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.ACCION.name());

        Element elementoTest5 = new Element("El Imperio Final", 
        "Nacidos de la bruma: El imperio final tiene lugar en un equivalente a principios del siglo xviii, en el distópico mundo de Scadrial, donde cae ceniza constantemente del cielo, las plantas son color café, y brumas sobrenaturales cubren el paisaje cada noche. Mil años antes del inicio de la novela, el profetizado Héroe de las Eras ascendió a la divinidad en el Pozo de la ascensión para repeler la Profundidad, un terror que acecha el mundo, cuya naturaleza real se ha perdido con el tiempo. Aunque la Profundidad fue exitosamente repelida y la humanidad se salvó, el mundo fue cambiado a su forma actual por el Héroe, quién tomó el título Lord Legislador y ha gobernado sobre el Imperio Final por mil años como un tirano inmortal y dios. Bajo su reinado, la sociedad fue estratificada en la nobleza, que se cree que fueron los descendientes de los amigos y aliados que le ayudaron a conseguir la divinidad, y los skaa, el campesinado brutalmente oprimido que desciende de aquellos que se opusieron a él.", 
        "Brandon Sanderson", Types.LIBRO.name(), Seasons.VERANO.name(), 
        States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1);
        elementRepository.save(elementoTest5);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.CIENCIAFICCION.name());
        generosEjemplo1.add(Genres.ACCION.name());

        Element elementoTest6 = new Element("Solo Leveling", 
        "En un mundo en el que ciertos humanos llamados «cazadores» poseen habilidades mágicas, estos deben luchar contra monstruos para proteger a la raza humana de una aniquilación segura. Un cazador muy débil llamado Sung Jinwoo se encuentra en una lucha en la que solo puede tratar de sobrevivir. Un día, después de sobrevivir por poco a una mazmorra doble abrumadoramente poderosa que casi acaba con todo su grupo, un programa misterioso llamado Sistema lo elige como su único jugador y, a su vez, le da la sorprendente habilidad de subir de nivel sin límites. Durante su viaje, Jinwoo luchará contra todo tipo de enemigos, tanto humanos como monstruos, y descubrirá los secretos que entrañan las mazmorras y la verdadera fuente de sus poderes.", 
        "Chugong", Types.LIBRO.name(), Seasons.PRIMAVERA.name(), 
        States.COMPLETO.name(), Countries.COREA.name(), generosEjemplo1);
        elementRepository.save(elementoTest6);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.MISTERIO.name());
        generosEjemplo1.add(Genres.NOVELA.name());

        Element elementoTest7 = new Element("El Signo de los Cuatro", 
        "En Inglaterra, a fines del siglo xix, Tras la misteriosa desaparición de su padre, Mary empieza a recibir valiosas perlas de un remitente desconocido. Después de un prolongado silencio, el generoso personaje da señales de vida y quiere que Mary se reúna con él. La joven pide ayuda a Sherlock Holmes para que la acompañe. El desconocido resulta ser Thaddeus Sholto, hijo de un buen amigo del padre de Mary. Thaddeus y su hermano han estado buscando, durante seis años, un gran tesoro que su padre escondió antes de morir. Por fin, tras un gran esfuerzo, han encontrado el tesoro, que, siguiendo las voluntades de su padre, deben compartir con Mary. Cuando llegan a la residencia de los Sholto, el hermano de Thaddeus ha sido asesinado y el tesoro robado. Tras esta trama comienza la gran búsqueda por los ladrones del mismo tesoro.", 
        "Arthur Conan Doyle", Types.LIBRO.name(), Seasons.INVIERNO.name(), 
        States.COMPLETO.name(), Countries.REINO_UNIDO.name(), generosEjemplo1);
        elementRepository.save(elementoTest7);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.NOVELA.name());
        generosEjemplo1.add(Genres.CIENCIAFICCION.name());

        Element elementoTest8 = new Element("El Problema de los Tres Cuerpos", 
        "Yu Wenjie, astrofísica víctima de las purgas de la Revolución, se instala en la cordillera Gran Khingan, en Pico Radar, para colaborar con el proyecto ultrasecreto Costa Roja. Paralelamente, el videojuego de realidad virtual Los Tres Cuerpos sumerge al jugador en una civilización llamada Trisolariana, que intenta sobrevivir en un planeta que orbita alrededor de tres estrellas.", 
        "Cixin Liu", Types.LIBRO.name(), Seasons.INVIERNO.name(), 
        States.COMPLETO.name(), Countries.CHINA.name(), generosEjemplo1);
        elementRepository.save(elementoTest8);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.NOVELA.name());
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.AVENTURA.name());

        Element elementoTest9 = new Element("Harry Potter y la Piedra Filosofal", 
        "Harry Potter crece en la casa de sus tíos, los Dursley, quienes le ocultan su verdadera historia familiar; al cumplir Harry once años de edad, empiezan a llegarle cartas de remitente desconocido, que van aumentando en número a medida que sus tíos no dejan que las abra. Las mismas traen la noticia de que el niño ha sido admitido en el Colegio Hogwarts de Magia y Hechicería, ya que, al igual que sus padres, es mago.", 
        "J.K. Rowling", Types.LIBRO.name(), Seasons.VERANO.name(), 
        States.COMPLETO.name(), Countries.REINO_UNIDO.name(), generosEjemplo1);
        elementRepository.save(elementoTest9);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest10 = new Element("Jujutsu Kaisen", 
        "En Jujutsu Kaisen, todos los seres vivos emanan una energía llamada Energía Maldita (呪力, Juryoku), que brota de las emociones negativas que fluyen naturalmente por el cuerpo. Las personas normales no tienen la habilidad de controlar este flujo en sus cuerpos. Como resultado, liberan continuamente esta Energía Maldita, lo que da lugar a que surjan las Maldiciones (呪い, Noroi), una raza de seres espirituales cuyo principal deseo es el de hacerle daño a la humanidad.", 
        "Gege Akutami", Types.LIBRO.name(), Seasons.PRIMAVERA.name(), 
        States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1);
        elementRepository.save(elementoTest10);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.JUVENIL.name());
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.DRAMA.name());

        Element elementoTest11 = new Element("Nunca Seré tu Héroe", 
        "Andrés está harto del colegio, de los granos, del rollo de su madre, de la plasta de su hermana, de Jorge el birlanovias, de la vida en general y del profe de Historia en particular. Dani y él se han propuesto cambiar el mundo y hacen un conjuro que les hará inmunes a las fantasmadas de los pijos, pelotas y demás aves carroñeras, y los convertirá en héroes. Bueno, al menos eso es lo que estaba previsto. Si luego las cosas se tuercen...", 
        "Maria Menendez Ponte", Types.LIBRO.name(), Seasons.OTOÑO.name(), 
        States.COMPLETO.name(), Countries.ESPAÑA.name(), generosEjemplo1);
        elementRepository.save(elementoTest11);
    }

}