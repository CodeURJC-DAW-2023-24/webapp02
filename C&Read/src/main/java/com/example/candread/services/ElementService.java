package com.example.candread.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
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

    // @PostConstruct
    public void insertElement() throws IOException, SerialException, SQLException {

        // String imagen2 = new String("static/Images/Alas_Sangre.jpg");

        // LISTS NEEDED FOR THE CONSTRUCTORS
        List<String> generosEjemplo1 = new ArrayList<>();
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.ROMANCE.name());
        List<User> userList = new ArrayList<>();
        List<Element> elementList = new ArrayList<>();

        // BASE USERS ON THE SYSTEM
        Optional<User> userPrueba3 = userRepository.findById((long) 1);
        User admin1 = userPrueba3.orElseThrow();
        Optional<User> userPrueba2 = userRepository.findById((long) 2);
        User admin2 = userPrueba2.orElseThrow();
        Optional<User> userPrueba1 = userRepository.findById((long) 3);
        User antonio = userPrueba1.orElseThrow();

        // Getting the info for the imageFile attribute
        // URL urlImg = new
        // URL("https://m.media-amazon.com/images/I/91OI4F8Fa7L._AC_UF894,1000_QL80_.jpg");
        Blob blobi = getBlob("static/Images/Alas_Sangre.jpg");
        Blob blobOnepiece = getBlob("static/Images/One_Piece_Libro.jpg");
        Blob blobMarina = getBlob("static/Images/Marina.jpg");
        Blob blobImperioFinal = getBlob("static/Images/El_Imperio_Final.jpg");
        Blob blobSoloLeveling = getBlob("static/Images/Solo_Leveling.jpeg");
        Blob blobSignoCuatro = getBlob("static/Images/El_Signo_DeLos_Cuatro.jpg");
        Blob blobTresCuerpos = getBlob("static/Images/El_Problema_DeLos_TresCuerpos.jpg");
        Blob blobHarryPotter = getBlob("static/Images/Harry_Potter_ y_la_Piedra_Filosofal.jpg");
        Blob blobJujutsu = getBlob("static/Images/Jujutsu_Kaisen.jpg");
        Blob blobHeroe = getBlob("static/Images/Nunca_Sere_Tu_Heroe.jpg");

        Blob undeadBlob = getBlob("static/Images/UndeadUnluck.jpg");

        // ClassPathResource imgFile = new
        // ClassPathResource("static/img/Alas_Sangre.jpg");
        // byte[] photoBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
        // Blob blobi = new SerialBlob(photoBytes);

        // DDBB CONSTRUCTORS FOR ELEMENTS
        Element elementoTest1 = new Element("Alas de Sangre",
                "Vuela... o muere. El nuevo fenómeno de fantasía juvenil del que todo el mundo habla.",
                "Rebecca Yarros", Types.LIBRO.name(), Seasons.OTOÑO.name(), States.COMPLETO.name(),
                Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 2023);

        elementoTest1.setImageFile(blobi);

        Review reviewTest1 = new Review("Viva el romantasy", 5);
        reviewTest1.setElementLinked(elementoTest1);
        reviewTest1.setUserLinked(antonio);
        userList.add(antonio);
        elementoTest1.setUsers(userList);

        elementoTest1.setUsersFavourited(userList);

        // elementList.add(elementoTest1);
        // antonio.getFavourites().addAll(elementList);
        // antonio.setFavourites(elementList);
        // userRepository.save(antonio);

        elementRepository.save(elementoTest1);

        elementList.clear();
        // elementList.add(elementoTest1);
        // elementoTest1.setUserFavourited(antonio);
        // antonio.setFavourites(elementList);

        reviewRepository.save(reviewTest1);

        generosEjemplo1.clear();
        userList.clear();
        userList.add(admin1);
        // reviewsEjemplo.clear();
        generosEjemplo1.add(Genres.AVENTURA.name());
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        userList.add(antonio);
        userList.add(admin2);

        Element elementoTest2 = new Element("One Piece",
                "La historia de un chaval buscando ser el rey de los piratas",
                "Eiichiro Oda", Types.LIBRO.name(), Seasons.VERANO.name(),
                States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1, 1997);
        elementoTest2.setImageFile(blobOnepiece);
        elementoTest2.setUsers(userList);
        elementRepository.save(elementoTest2);

        Review reviewTest3 = new Review(
                "La tripulación es súper animada y la historia parece no acabar nunca, chopper es monisimo uwu", 4);
        reviewTest3.setElementLinked(elementoTest2);
        reviewRepository.save(reviewTest3);
        Review reviewTest2 = new Review("BUENARDO EL MANGA PAPU", 5);
        reviewTest2.setElementLinked(elementoTest2);
        reviewRepository.save(reviewTest2);

        generosEjemplo1.clear();
        userList.clear();
        userList.add(antonio);
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.AVENTURA.name());

        Element elementoTest3 = new Element("Undead Unluck",
                "La historia de la búsqueda de la mayor muerte entre un no-muerto y una gafe.",
                "Yoshifumi Tozuka", Types.LIBRO.name(), Seasons.PRIMAVERA.name(),
                States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1, 2021);
        elementoTest3.setImageFile(undeadBlob);
        elementoTest3.setUsers(userList);
        elementRepository.save(elementoTest3);

        generosEjemplo1.clear();
        userList.clear();
        generosEjemplo1.add(Genres.NOVELA.name());
        generosEjemplo1.add(Genres.MISTERIO.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest4 = new Element("Marina",
                "La trama se desarrolla en la segunda mitad del siglo xx y narra dos historias paralelas. La principal es la historia de Óscar y Marina, enternecedora y emotiva. Después, se centra en la enigmática vida y empresa de Mijail Kolvenik, un genio en la creación de artículos de ortopedia y prótesis médicas, que motivado por la locura de superar a la muerte y los errores de las deformaciones humanas termina convirtiéndose en una horrible bestia",
                "Carlos Luis Zafon", Types.LIBRO.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.ESPAÑA.name(), generosEjemplo1, 1999);
        elementoTest4.setImageFile(blobMarina);
        elementRepository.save(elementoTest4);

        // MAKING A BOOK A FAVOURITE FOR ANTONIO
        // elementList.add(elementoTest1);
        elementList.add(elementoTest4);
        antonio.setFavourites(elementList);
        userRepository.save(antonio);
        // antonio.getFavourites().add(elementoTest4);

        // elementList.clear();
        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.ACCION.name());

        Element elementoTest5 = new Element("El Imperio Final",
                "Nacidos de la bruma: El imperio final tiene lugar en un equivalente a principios del siglo xviii, en el distópico mundo de Scadrial, donde cae ceniza constantemente del cielo, las plantas son color café, y brumas sobrenaturales cubren el paisaje cada noche. Mil años antes del inicio de la novela, el profetizado Héroe de las Eras ascendió a la divinidad en el Pozo de la ascensión para repeler la Profundidad, un terror que acecha el mundo, cuya naturaleza real se ha perdido con el tiempo. Aunque la Profundidad fue exitosamente repelida y la humanidad se salvó, el mundo fue cambiado a su forma actual por el Héroe, quién tomó el título Lord Legislador y ha gobernado sobre el Imperio Final por mil años como un tirano inmortal y dios. Bajo su reinado, la sociedad fue estratificada en la nobleza, que se cree que fueron los descendientes de los amigos y aliados que le ayudaron a conseguir la divinidad, y los skaa, el campesinado brutalmente oprimido que desciende de aquellos que se opusieron a él.",
                "Brandon Sanderson", Types.LIBRO.name(), Seasons.VERANO.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 2006);
        elementoTest5.setImageFile(blobImperioFinal);
        elementRepository.save(elementoTest5);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.CIENCIAFICCION.name());
        generosEjemplo1.add(Genres.ACCION.name());

        Element elementoTest6 = new Element("Solo Leveling",
                "En un mundo en el que ciertos humanos llamados «cazadores» poseen habilidades mágicas, estos deben luchar contra monstruos para proteger a la raza humana de una aniquilación segura. Un cazador muy débil llamado Sung Jinwoo se encuentra en una lucha en la que solo puede tratar de sobrevivir. Un día, después de sobrevivir por poco a una mazmorra doble abrumadoramente poderosa que casi acaba con todo su grupo, un programa misterioso llamado Sistema lo elige como su único jugador y, a su vez, le da la sorprendente habilidad de subir de nivel sin límites. Durante su viaje, Jinwoo luchará contra todo tipo de enemigos, tanto humanos como monstruos, y descubrirá los secretos que entrañan las mazmorras y la verdadera fuente de sus poderes.",
                "Chugong", Types.LIBRO.name(), Seasons.PRIMAVERA.name(),
                States.COMPLETO.name(), Countries.COREA.name(), generosEjemplo1, 2016);
        elementoTest6.setImageFile(blobSoloLeveling);
        elementRepository.save(elementoTest6);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.MISTERIO.name());
        generosEjemplo1.add(Genres.NOVELA.name());

        Element elementoTest7 = new Element("El Signo de los Cuatro",
                "En Inglaterra, a fines del siglo xix, Tras la misteriosa desaparición de su padre, Mary empieza a recibir valiosas perlas de un remitente desconocido. Después de un prolongado silencio, el generoso personaje da señales de vida y quiere que Mary se reúna con él. La joven pide ayuda a Sherlock Holmes para que la acompañe. El desconocido resulta ser Thaddeus Sholto, hijo de un buen amigo del padre de Mary. Thaddeus y su hermano han estado buscando, durante seis años, un gran tesoro que su padre escondió antes de morir. Por fin, tras un gran esfuerzo, han encontrado el tesoro, que, siguiendo las voluntades de su padre, deben compartir con Mary. Cuando llegan a la residencia de los Sholto, el hermano de Thaddeus ha sido asesinado y el tesoro robado. Tras esta trama comienza la gran búsqueda por los ladrones del mismo tesoro.",
                "Arthur Conan Doyle", Types.LIBRO.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.REINO_UNIDO.name(), generosEjemplo1, 1890);
        elementoTest7.setImageFile(blobSignoCuatro);
        elementRepository.save(elementoTest7);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.NOVELA.name());
        generosEjemplo1.add(Genres.CIENCIAFICCION.name());

        Element elementoTest8 = new Element("El Problema de los Tres Cuerpos",
                "Yu Wenjie, astrofísica víctima de las purgas de la Revolución, se instala en la cordillera Gran Khingan, en Pico Radar, para colaborar con el proyecto ultrasecreto Costa Roja. Paralelamente, el videojuego de realidad virtual Los Tres Cuerpos sumerge al jugador en una civilización llamada Trisolariana, que intenta sobrevivir en un planeta que orbita alrededor de tres estrellas.",
                "Cixin Liu", Types.LIBRO.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.CHINA.name(), generosEjemplo1, 2006);
        elementoTest8.setImageFile(blobTresCuerpos);

        userList.add(antonio);
        elementoTest8.setUsers(userList);
        elementRepository.save(elementoTest8);

        generosEjemplo1.clear();
        userList.clear();
        generosEjemplo1.add(Genres.NOVELA.name());
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.AVENTURA.name());

        Element elementoTest9 = new Element("Harry Potter y la Piedra Filosofal",
                "Harry Potter crece en la casa de sus tíos, los Dursley, quienes le ocultan su verdadera historia familiar; al cumplir Harry once años de edad, empiezan a llegarle cartas de remitente desconocido, que van aumentando en número a medida que sus tíos no dejan que las abra. Las mismas traen la noticia de que el niño ha sido admitido en el Colegio Hogwarts de Magia y Hechicería, ya que, al igual que sus padres, es mago.",
                "J.K. Rowling", Types.LIBRO.name(), Seasons.VERANO.name(),
                States.COMPLETO.name(), Countries.REINO_UNIDO.name(), generosEjemplo1, 1997);
        elementoTest9.setImageFile(blobHarryPotter);
        elementRepository.save(elementoTest9);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest10 = new Element("Jujutsu Kaisen",
                "En Jujutsu Kaisen, todos los seres vivos emanan una energía llamada Energía Maldita (呪力, Juryoku), que brota de las emociones negativas que fluyen naturalmente por el cuerpo. Las personas normales no tienen la habilidad de controlar este flujo en sus cuerpos. Como resultado, liberan continuamente esta Energía Maldita, lo que da lugar a que surjan las Maldiciones (呪い, Noroi), una raza de seres espirituales cuyo principal deseo es el de hacerle daño a la humanidad.",
                "Gege Akutami", Types.LIBRO.name(), Seasons.PRIMAVERA.name(),
                States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1, 2018);
        elementoTest10.setImageFile(blobJujutsu);
        elementRepository.save(elementoTest10);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.JUVENIL.name());
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.DRAMA.name());

        Element elementoTest11 = new Element("Nunca Seré tu Héroe",
                "Andrés está harto del colegio, de los granos, del rollo de su madre, de la plasta de su hermana, de Jorge el birlanovias, de la vida en general y del profe de Historia en particular. Dani y él se han propuesto cambiar el mundo y hacen un conjuro que les hará inmunes a las fantasmadas de los pijos, pelotas y demás aves carroñeras, y los convertirá en héroes. Bueno, al menos eso es lo que estaba previsto. Si luego las cosas se tuercen...",
                "Maria Menendez Ponte", Types.LIBRO.name(), Seasons.OTOÑO.name(),
                States.COMPLETO.name(), Countries.ESPAÑA.name(), generosEjemplo1, 1998);
        elementoTest11.setImageFile(blobHeroe);
        elementRepository.save(elementoTest11);

    }

    public void insertSeries() throws SerialException, IOException, SQLException {

        Blob blobOnepiece = getBlob("static/Images/CardCover-OnePiece.jpg");
        Blob blobSoloLeveling = getBlob("static/Images/Solo_Leveling_Serie.jpg");
        Blob blobJujutsu = getBlob("static/Images/Jujutsu_Kaisen.jpg");
        Blob undeadBlob = getBlob("static/Images/UndeadUnluck_Serie.jpg");
        Blob blobFrieren = getBlob("static/Images/Frieren.jpg");
        Blob blobFull = getBlob("static/Images/Full_Metal_Alchemist.jpg");
        Blob blobMagi = getBlob("static/Images/Magi_Simbad.jpg");
        Blob blobSimpson = getBlob("static/Images/Simpson.jpg");
        Blob blobi = getBlob("static/Images/AquiNoHayQuienViva.jpg");
        Blob blobArcane = getBlob("static/Images/Arcane.jpg");
        Blob blobi2 = getBlob("static/Images/LaQueSeAvecina.jpg");

        List<String> generosEjemplo1 = new ArrayList<>();

        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.CIENCIAFICCION.name());
        generosEjemplo1.add(Genres.ACCION.name());

        // BASE USERS ON THE SYSTEM
        Optional<User> userPrueba2 = userRepository.findById((long) 2);
        User admin2 = userPrueba2.orElseThrow();
        Optional<User> userPrueba1 = userRepository.findById((long) 3);
        User antonio = userPrueba1.orElseThrow();
        List<User> userList = new ArrayList<>();

        userList.add(antonio);

        Element elementoTest1 = new Element("Solo Leveling",
                "En un mundo en el que ciertos humanos llamados «cazadores» poseen habilidades mágicas, estos deben luchar contra monstruos para proteger a la raza humana de una aniquilación segura. Un cazador muy débil llamado Sung Jinwoo se encuentra en una lucha en la que solo puede tratar de sobrevivir. Un día, después de sobrevivir por poco a una mazmorra doble abrumadoramente poderosa que casi acaba con todo su grupo, un programa misterioso llamado Sistema lo elige como su único jugador y, a su vez, le da la sorprendente habilidad de subir de nivel sin límites. Durante su viaje, Jinwoo luchará contra todo tipo de enemigos, tanto humanos como monstruos, y descubrirá los secretos que entrañan las mazmorras y la verdadera fuente de sus poderes.",
                "Chugong", Types.SERIE.name(), Seasons.PRIMAVERA.name(),
                States.COMPLETO.name(), Countries.COREA.name(), generosEjemplo1, 2024);
        elementoTest1.setImageFile(blobSoloLeveling);
        elementoTest1.setUsers(userList);
        elementRepository.save(elementoTest1);

        generosEjemplo1.clear();
        userList.clear();
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.AVENTURA.name());

        Element elementoTest2 = new Element("One Piece",
                "La historia de un chaval buscando ser el rey de los piratas",
                "Eiichiro Oda", Types.SERIE.name(), Seasons.VERANO.name(),
                States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1, 1999);
        elementoTest2.setImageFile(blobOnepiece);
        elementRepository.save(elementoTest2);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.AVENTURA.name());

        Element elementoTest3 = new Element("Undead Unluck",
                "La historia de la búsqueda de la mayor muerte entre un no-muerto y una gafe.",
                "Yoshifumi Tozuka", Types.SERIE.name(), Seasons.PRIMAVERA.name(),
                States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1, 2023);
        elementoTest3.setImageFile(undeadBlob);

        userList.add(antonio);
        userList.add(admin2);
        elementoTest3.setUsers(userList);
        elementRepository.save(elementoTest3);

        generosEjemplo1.clear();
        userList.clear();
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.MISTERIO.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest4 = new Element("FullMetal Alchemist Brotherhood",
                "La historia se centra en dos hermanos, Edward Elric y Alphonse Elric que rompieron el mayor tabú de la alquimia, la trasmutación humana al tratar de revivir a su fallecida madre; en consecuencia Edward pierde su pierna izquierda y Alphonse pierde todo su cuerpo, Edward para salvar a su hermano sella su alma en una gran armadura de hierro a cambio de su brazo derecho; ahora los dos con un nuevo objetivo buscan desesperadamente la piedra filosofal para poder regresar sus cuerpos a la normalidad...",
                "Carlos Luis Zafon", Types.SERIE.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.ESPAÑA.name(), generosEjemplo1, 2009);
        elementoTest4.setImageFile(blobFull);
        elementRepository.save(elementoTest4);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.SOBRENATURAL.name());
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest5 = new Element("Jujutsu Kaisen",
                "En Jujutsu Kaisen, todos los seres vivos emanan una energía llamada Energía Maldita (呪力, Juryoku), que brota de las emociones negativas que fluyen naturalmente por el cuerpo. Las personas normales no tienen la habilidad de controlar este flujo en sus cuerpos. Como resultado, liberan continuamente esta Energía Maldita, lo que da lugar a que surjan las Maldiciones (呪い, Noroi), una raza de seres espirituales cuyo principal deseo es el de hacerle daño a la humanidad.",
                "Gege Akutami", Types.SERIE.name(), Seasons.PRIMAVERA.name(),
                States.EN_EMISION.name(), Countries.JAPON.name(), generosEjemplo1, 2020);
        elementoTest5.setImageFile(blobJujutsu);
        elementRepository.save(elementoTest5);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.AVENTURA.name());

        Element elementoTest6 = new Element("Frieren: Más allá del final del viaje",
                "La maga Frieren formaba parte del grupo del héroe Himmel, quienes derrotaron al Rey Demonio tras un viaje de 10 años y devolvieron la paz al reino. Frieren es una elfa de más de mil años de vida, así que al despedirse de Himmel y sus compañeros promete que regresará para verlos y parte de viaje sola. Al cabo de cincuenta años, Frieren cumple su promesa y acude a visitar a Himmel y al resto. Aunque ella no ha cambiado, Himmel y los demás han envejecido y están en el final de sus vidas. Cuando Himmel muere, Frieren se arrepiente de no haber pasado más tiempo a su lado conociéndolo, así que emprende un viaje para conocer mejor a sus antiguos compañeros, a las personas y descubrir más del mundo.",
                "Keiichirou Saitou", Types.SERIE.name(), Seasons.PRIMAVERA.name(),
                States.COMPLETO.name(), Countries.COREA.name(), generosEjemplo1, 2023);
        elementoTest6.setImageFile(blobFrieren);

        userList.clear();
        userList.add(antonio);
        elementoTest6.setUsersFavourited(userList);

        elementRepository.save(elementoTest6);
        userList.clear();

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.AVENTURA.name());
        generosEjemplo1.add(Genres.ACCION.name());

        Element elementoTest7 = new Element("Magi: Sinbad no Bouken",
                "La historia tiene lugar 30 años antes de los acontecimientos de Magi. En esta era nos centraremos en el viaje que tiene que realizar Sinbad para convertirse en rey.",
                "Arthur Conan Doyle",
                Types.SERIE.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.REINO_UNIDO.name(), generosEjemplo1, 2016);
        elementoTest7.setImageFile(blobMagi);
        elementRepository.save(elementoTest7);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.COMEDIA.name());

        Element elementoTest8 = new Element("Los Simpson",
                "Es una serie estadounidense de comedia en formato de animación, creada por Matt Groening para Fox Broadcasting Company y emitida en varios países del mundo. La serie es una sátira de la sociedad estadounidense que narra la vida y el día a día de una familia de clase media de ese país cuyos miembros son Homer, Marge, Bart, Lisa y Maggie Simpson que vive en un pueblo ficticio llamado Springfield",
                "Matt Groening", Types.SERIE.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.CHINA.name(), generosEjemplo1, 1989);
        elementoTest8.setImageFile(blobSimpson);
        elementRepository.save(elementoTest8);

        Element elementoTest9 = new Element("Aquí no hay quien viva",
                "Es una serie de televisión española de género humorístico emitida en Antena 3 entre el 7 de septiembre de 2003 y el 6 de julio de 2006. Narra la vida de una peculiar comunidad de vecinos de Desengaño 21: tres pisos, dos casas por planta, un ático, una portería y un local contiguo.​ Se desconoce el verdadero artífice de la idea. La serie fue creada por Iñaki Ariztimuño, los hermanos Laura y Alberto Caballero sobrinos de José Luis Moreno, productor ejecutivo de la serie.3​ Actualmente se considera serie de culto",
                "Iñaki Ariztimuño", Types.SERIE.name(), Seasons.VERANO.name(),
                States.COMPLETO.name(), Countries.ESPAÑA.name(), generosEjemplo1, 2003);
        elementoTest9.setImageFile(blobi);
        elementRepository.save(elementoTest9);

        Element elementoTest10 = new Element("La que se avecina",
                "Es una serie de televisión española creada por los hermanos Alberto y Laura Caballero y por Daniel Deorador para Telecinco, que se estrenó el 22 de abril de 2007. A partir de la duodécima temporada, debido a un acuerdo entre Mediaset España y Prime Video, también se preestrena en la plataforma de streaming antes de su emisión en Telecinco.",
                "Alberto Caballero", Types.SERIE.name(), Seasons.PRIMAVERA.name(),
                States.EN_EMISION.name(), Countries.ESPAÑA.name(), generosEjemplo1, 2007);
        elementoTest10.setImageFile(blobi2);
        elementRepository.save(elementoTest10);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.DRAMA.name());
        generosEjemplo1.add(Genres.MISTERIO.name());

        Element elementoTest11 = new Element("Arcane",
                "La historia se desarrollará en las ciudades de Piltover y Zaun. Aunque Arcane se centra solo en estas dos ciudades, estas pertenecen al mundo Runeterra. Piltover es la ciudad académica y científica y adinerada, mientras que Zaun es todo lo contrario, con suburbios llenos de criminalidad, pobreza e infestado de productos químicos tóxicos, donde no hay ningún tipo de autoridad. Las tensiones y rivalidades que existen entre estas dos ciudades divide a familias y amigos como en el caso de Vi y Powder, dos hermanas zaunitas; o de Jayce y Viktor, dos amigos inventores y aprendices del legendario científico Heimerdinger. Todas estas relaciones se desbordarán con la creación de Hextech en Piltover, una tecnología con la que cualquier persona es capaz de controlar la energía mágica, mientras que en Zaun una nueva droga transforma a los humanos en monstruos.",
                "Christian Linke", Types.SERIE.name(), Seasons.OTOÑO.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 2021);
        elementoTest11.setImageFile(blobArcane);
        elementRepository.save(elementoTest11);

    }

    public void inserFilms() throws SerialException, IOException, SQLException {

        Blob blobAladdin = getBlob("static/Images/Aladdin.jpg");
        Blob blobBeaty = getBlob("static/Images/Beauty_And_The_Beast.jpg");
        Blob blobElemental = getBlob("static/Images/Elemental.jpg");
        Blob blobNotredam = getBlob("static/Images/Jorobado.jpg");
        Blob blobLightyear = getBlob("static/Images/Lightyear.jpg");
        Blob blobMoana = getBlob("static/Images/Moana.jpg");
        Blob blobPocahontas = getBlob("static/Images/Pocahontas.jpg");
        Blob blobSimpson = getBlob("static/Images/Los_Simpson_La_pelicula.jpg");
        Blob blobLionKIng = getBlob("static/Images/Rey_Leon.jpg");
        Blob blobMermaid = getBlob("static/Images/Sirenita.jpg");
        Blob blobUp = getBlob("static/Images/Up.jpg");

        // BASE USERS ON THE SYSTEM
        Optional<User> userPrueba3 = userRepository.findById((long) 1);
        User admin1 = userPrueba3.orElseThrow();
        Optional<User> userPrueba1 = userRepository.findById((long) 3);
        User antonio = userPrueba1.orElseThrow();
        List<User> userList = new ArrayList<>();

        List<String> generosEjemplo1 = new ArrayList<>();

        generosEjemplo1.add(Genres.AVENTURA.name());
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.INFANTIL.name());

        Element elementoTest1 = new Element("Aladdin",
                "La historia comienza con el intento de Jafar, gran visir del sultán de la ficticia ciudad de Agrabah, de acceder a la Cueva de las Maravillas, ya que en su interior hay una lámpara de aceite que contiene un genio. No obstante, este y su loro Iago necesitan de Aladdín para poder entrar en la cueva. A la par de esta situación, Jasmín, hija del Sultán, huye del palacio debido a que discrepa sobre su obligación de casarse. Es allí donde se encuentra con el joven callejero, Aladdín, y su mono Abu, pero ambos son capturados por el visir.",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.PRIMAVERA.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 1992);
        elementoTest1.setImageFile(blobAladdin);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.JUVENIL.name());
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest2 = new Element("La bella y la bestia",
                "Una hechicera disfrazada de mendiga visita el castillo de un príncipe cruel y egoísta, ofreciéndole una rosa encantada a cambio de refugio ante una tormenta. Cuando él se niega, ella revela su identidad y transforma al príncipe en una bestia y a sus sirvientes en objetos domésticos. Ella advierte al príncipe que el hechizo sólo se romperá si aprende a amar a otro y a ser amado a cambio antes de que caiga el último pétalo, o seguirá siendo una bestia para siempre.",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.VERANO.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 1992);
        elementoTest2.setImageFile(blobBeaty);
        elementRepository.save(elementoTest2);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.FANTASIA.name());
        generosEjemplo1.add(Genres.INFANTIL.name());

        Element elementoTest3 = new Element("Elemental",
                "Los Elementos de fuego Lucio y Ceni emigran a Ciudad Elemento, donde se enfrentan a la xenofobia de otros elementos y luchan por encontrar un hogar.\r\n"
                        + //
                        "\r\n" + //
                        "Después del nacimiento de su hija Candela, construyen su propia tienda llamada El Hogar y establecen una Llama Azul que representa sus tradiciones familiares. Candela crece trabajando en la tienda con Lucio, quien tiene la intención de legarle la tienda una vez que él se jubile, pero primero debe controlar su temperamento explosivo.",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.PRIMAVERA.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 2023);
        elementoTest3.setImageFile(blobElemental);

        userList.add(antonio);
        userList.add(admin1);
        elementoTest3.setUsers(userList);
        elementRepository.save(elementoTest3);

        generosEjemplo1.clear();
        userList.clear();
        generosEjemplo1.add(Genres.MISTERIO.name());
        generosEjemplo1.add(Genres.DRAMA.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest4 = new Element("El Jorobado de Notre Dam",
                "La historia da inicio cuando el cruel Juez Claude Frollo comienza su encarnizada lucha contra los gitanos, entre ellos una gitana que trata de huir con algo entre sus brazos. Frollo, al creer que oculta bienes robados, comienza a perseguir a la mujer hasta luego asesinarla en la entrada de la catedral Notre Dame cuando esta se niega a entregar su carga. Sin embargo, descubre que los ‘‘bienes robados’’ era en realidad un bebé muy extraño: el niño es completamente deforme y jorobado de nacimiento. Frollo planeaba matarlo ahogándolo en un pozo, pero el archidiácono evita que se cometa el segundo crimen, y aconseja a Frollo cuidar al niño y criarlo como si fuera suyo, ya que de lo contrario enfrentaría la ira divina, pues la catedral había sido testigo de todo. Así es como acuerda refugiarlo en el campanario para que nadie pueda ver su monstruosa humanidad, y decide llamarlo Quasimodo que significa mal formado..",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 1996);
        elementoTest4.setImageFile(blobNotredam);
        elementRepository.save(elementoTest4);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.JUVENIL.name());
        generosEjemplo1.add(Genres.AVENTURA.name());

        Element elementoTest5 = new Element("LightYear",
                "Buzz Lightyear es un guardián espacial del Comando Estelar, el cual lleva a cabo una misión, junto con su oficial al mando y mejor amiga, Alisha Hawthorne, deciden aterrizar y explorar el planeta habitable, T'Kani Prime para corroborar si el mismo tiene las condiciones ideales para la colonización. Para esta misión de exploración, a ambos guardianes los acompaña un nuevo recluta llamado Featheringhamstan, con quien Buzz no desea trabajar por su inexperiencia en el campo, pero pronto se ven obligados a retirarse a su nave de exploración después de descubrir que el planeta alberga formas de vida hostiles. Creyendo poder dirigir la nave principal conocida como el Nabo, Buzz decide ignorar al asistente de vuelo y daña accidentalmente la embarcación durante la retirada cuando roza la punta de un acantilado, lo que a su vez provoca un daño en el motor y los obliga a aterrizar forzosamente en el planeta, tras el choque la tripulación es despertada del híper sueño y es ordenada a evacuar para realizar reparaciones, pero desafortunadamente el choque termina por dañar el cristal de combustible y sin el mismo no podrán continuar su viaje.",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.PRIMAVERA.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 2022);
        elementoTest5.setImageFile(blobLightyear);
        elementRepository.save(elementoTest5);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.COMEDIA.name());
        generosEjemplo1.add(Genres.AVENTURA.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest6 = new Element("Moana",
                "Hace tres mil años, en la isla de Motului, los habitantes adoran a la diosa Te Fiti, quien trajo vida al océano usando una piedra pounamu como su corazón y la fuente de su poder. Maui, el semidiós que cambia de forma del viento y el mar y maestro de la navegación, roba el corazón para darle a la humanidad el poder de la creación. Sin embargo, Te Fiti se desintegra, y Maui es atacado por otro que busca el corazón, Te Kā, un demonio volcánico. Pierde tanto su mágico anzuelo gigante como el corazón en las profundidades del mar.",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.PRIMAVERA.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 2016);
        elementoTest6.setImageFile(blobMoana);

        userList.clear();
        userList.add(antonio);
        elementoTest6.setUsersFavourited(userList);
        elementRepository.save(elementoTest6);
        userList.clear();

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.AVENTURA.name());
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.JUVENIL.name());

        Element elementoTest7 = new Element("Pocahontas",
                "En el Londres de 1607 un barco de colonos británicos emprende rumbo hacia el Nuevo Mundo en busca de glorias y riquezas. En la nave va el Capitán John Smith junto a otros colonos. Por su parte, Pocahontas, hija del jefe indígena Powhatan, se siente decepcionada con su destino cuando su padre la compromete con Kocoum, uno de los guerreros más fuertes de la tribu. La joven y sus dos mejores amigos Meeko, un mapache hambriento, y Flit, un colibrí testarudo, va a pedirle un consejo a la Abuela Sauce, un árbol milenario que posee poderes mágicos y sabiduría (y que ya había sido la guía espiritual de su madre, fallecida años atrás); así mismo, le cuenta que durante todas noches se le repite un sueño, en donde veía una flecha que giraba y giraba, y de pronto se detenía apuntando una dirección.",
                "Walt Disney Pictures",
                Types.PELICULA.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 1995);
        elementoTest7.setImageFile(blobPocahontas);
        elementRepository.save(elementoTest7);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.DRAMA.name());
        generosEjemplo1.add(Genres.JUVENIL.name());
        generosEjemplo1.add(Genres.INFANTIL.name());

        Element elementoTest8 = new Element("El Rey Leon",
                "En Pride Lands,el león Mufasa gobierna a todos los animales como su rey junto con su esposa Sarabi. El nacimiento de su hijo y heredero, Simba, provoca resentimiento en el hermano de Mufasa, Scar, que anhela convertirse en el nuevo rey. El tiempo transcurre y el cachorro es instruido por su padre sobre las responsabilidades que habrá de tener cuando se convierta en el nuevo soberano. Mientras tanto, Scar trama un siniestro plan para deshacerse de sus familiares y apoderarse del trono. Primeramente engaña a Simba y a su amiga Nala al incitarlos a explorar un cementerio de elefantes ubicado en una región apartada de Pride Lands. Pese a las advertencias de Mufasa sobre ese lugar, la curiosidad de Simba y Nala los lleva hasta ese sitio. Al llegar, son atacados por las hienas Shenzi, Banzai y Ed, quienes trabajan para Scar pese a los maltratos que reciben de su parte.",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.INVIERNO.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 1994);
        elementoTest8.setImageFile(blobLionKIng);
        elementRepository.save(elementoTest8);

        Element elementoTest9 = new Element("La Sirenita",
                "Ariel es una princesa sirena de 16 años de edad cuyo padre es el rey Tritón, el rey gobernante supremo del fondo del mar y del océano, y no está del todo feliz con su vida bajo el mar, pues tiene curiosidad de conocer el mundo de los seres humanos. Junto a su mejor amigo, el pez Flounder, Ariel colecciona artefactos humanos y con frecuencia acude a la superficie del océano para encontrarse con Scuttle, una gaviota, que le habla sobre el mundo humano aunque de forma poco acertada con la realidad. La joven princesa sirena Ariel ignora las advertencias de su padre, y de su amigo, el cangrejo Sebastián, quienes le dicen que el contacto entre el reino de las sirenas y los tritones y el reino de los humanos está prohibido por la crueldad de estos a la vida marina. Lo cierto es que Ariel sueña con la idea de convertirse en una chica humana y así vivir en ese mundo feliz con el hombre de sus sueños que tanto ha ilusionado.",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.VERANO.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 1989);
        elementoTest9.setImageFile(blobMermaid);
        elementRepository.save(elementoTest9);

        Element elementoTest10 = new Element("Up",
                "Las primeras escenas retoman la infancia de Carl Fredricksen, un niño tímido y serio que idolatra al famoso explorador Charles F. Muntz Jr, cuyas anécdotas suelen relatarse en unos informativos proyectados en las salas de cine de la época. Un día, tras acudir al cine para mirar las nuevas aventuras de Muntz, Carl se entera de que este fue acusado de fabricar el esqueleto de un ave gigante, el cual afirmó que había descubierto en Cataratas del Paraíso. Ante el hallazgo de su supuesta mentira, perdió su empleo. No obstante, se comprometió a regresar a ese sitio para traer consigo un ejemplar vivo de esa ave y así retomar su reputación. La revelación de la mentira de Muntz entristece al joven Carl.",
                "Walt Disney Pictures", Types.PELICULA.name(), Seasons.PRIMAVERA.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 2009);
        elementoTest10.setImageFile(blobUp);
        elementRepository.save(elementoTest10);

        generosEjemplo1.clear();
        generosEjemplo1.add(Genres.ACCION.name());
        generosEjemplo1.add(Genres.DRAMA.name());
        generosEjemplo1.add(Genres.MISTERIO.name());

        Element elementoTest11 = new Element("Los simpson la pelicula",
                "Al comienzo de la película, se muestra una caricatura de Itchy & Scratchy, en la cual en un viaje al espacio Itchy rompe el casco de Scratchy y lo deja morir mientras regresa a la Tierra, es electo presidente, pero al ver que Scratchy sigue vivo y con la intención de contar la verdad, Itchy lanza misiles nucleares con tal de matar definitivamente a Scratchy. De la nada, Homer Simpson se levanta y llama a todos en la sala de cine (pues resulta que lo que se muestra primero era una película) unos «pringaos» («crédulos» en Hispanoamérica), diciendo «en especial tú» refiriéndose al espectador, y dando inicio a la película.",
                "Matt Groening", Types.PELICULA.name(), Seasons.OTOÑO.name(),
                States.COMPLETO.name(), Countries.ESTADOS_UNIDOS.name(), generosEjemplo1, 2007);
        elementoTest11.setImageFile(blobSimpson);
        elementRepository.save(elementoTest11);

    }

    public Blob getBlob(String path) throws IOException, SerialException, SQLException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Resource resource = new ClassPathResource(path);
        InputStream inputStream = resource.getInputStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] imageBytes = outputStream.toByteArray();
        Blob blobi = new SerialBlob(imageBytes);
        return blobi;
    }

    public void fullSet64Image() throws SQLException, IOException {
        List<Element> elements = elementRepository.findAll();
        int size = elements.size();
        long longSize = size;
        for (long i = 1; i <= longSize; i++) {
            setElementsImage64(i);
        }
    }

    public void setElementsImage64(long id) throws SQLException, IOException {
        Optional<Element> elementOptional = elementRepository.findById(id);
        Element element = elementOptional.orElseThrow();
        Blob blob = element.getImageFile();
        InputStream inputStream = blob.getBinaryStream();
        byte[] imageBytes = inputStream.readAllBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        element.setBase64Image(base64Image);

        inputStream.close();
    }

}