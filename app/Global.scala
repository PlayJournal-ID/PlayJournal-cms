import play.api._
import models._
import anorm._

object Global extends GlobalSettings {
    override def onStart(app: Application) = {
        if (Privilege.findAll.isEmpty) {
            Seq(
                Privilege(Id(1), "admin"),
                Privilege(Id(2), "standard"),
                Privilege(Id(3), "writer"),
                Privilege(Id(4), "editor")) foreach Privilege.create
        }

        if (Users.findAll.isEmpty) {
            Seq(
                Users(Id(1), "user@playjournal.com", "user", "User", Privilege.standard),
                Users(Id(2), "admin@playjournal.com", "admin", "Admin", Privilege.admin),
                Users(Id(3), "writer@playjournal.com", "writer", "Writer", Privilege.writer),
                Users(Id(4), "editor@playjournal.com", "editor", "Editor", Privilege.editor)) foreach Users.create
        }

        if (Post.findAll.isEmpty) {
            for (id <- 1 to 50) {
                Post.create(
                    "This is a generated post " + id.toString,
                    postContent,
                    (id % 3) + 2
                )
            }
        }
    }

    val postContent = """In elementum platea a! Integer et parturient mid proin augue, tristique mus. Pulvinar mid velit sagittis ac nunc nascetur, eros tincidunt vut ridiculus porttitor vut mid lectus risus elementum ac, rhoncus porttitor nec ut massa? A ut, magna! Arcu augue non vel nunc turpis, non lundium egestas magnis habitasse ac nec mus vel? A. Sit cursus, quis dolor pulvinar porta dapibus aenean lacus, vut et hac eros augue velit pulvinar, non cursus, pellentesque velit aliquam.

## Heading

Lectus risus? Proin ultricies ac! Urna amet vel mauris, lacus, dapibus! Ultrices, aenean turpis, enim sit cursus odio quis urna nisi? Dapibus tristique cras arcu elementum integer sit, egestas adipiscing adipiscing dapibus in turpis mid quis. Nec porta turpis turpis augue. Purus dignissim aliquet? Non, auctor sed augue, phasellus augue porttitor diam porta a augue sit vel, dis est elementum. Vel urna! Augue vel! Dictumst turpis porta platea rhoncus urna in tincidunt enim amet:

1. lorem ipsum dolor sit amet
2. excorcisomus te
3. omis legios, omnis satanicus, omis spiritus, et cetera diabolica

Massa tincidunt aliquam phasellus magna, sagittis etiam elementum et aliquam rhoncus odio magna, risus odio nisi dis nisi turpis lectus nisi, proin eros, mattis integer augue aliquam auctor parturient? Ultricies ut nunc! Aliquam, sed tortor, et proin massa tincidunt, aliquet vel, mauris magna porttitor magna sed vel! Cras tincidunt? Velit ultricies diam est cras tristique a turpis. Rhoncus phasellus, augue eros tincidunt? Dictumst. Pulvinar egestas integer lundium sed augue sit cras sed natoque! Ac.

## Another Heading

[A link to the future][1]. This is the quote:

> I am not a hero. Never was, never will be
>> Solid Snake

This is a sample code block:

    val testing = test match {
         case s: String => s.substring(0, 10)
         case _         => "No String"
    }

This is text after code.

## We need a lot of headings!

Natoque vel. Ridiculus elit non pulvinar sit, integer magna vel ultrices elit, auctor egestas lacus ac nascetur a enim arcu. Phasellus elementum odio placerat? Mauris sagittis augue tempor dapibus mattis, mid arcu rhoncus elementum eros integer, lectus urna, tortor magna risus ultricies lorem quis magna diam vel lundium. Mid, turpis nunc. Ultricies lectus placerat? Sagittis! Scelerisque? Dignissim adipiscing, lacus integer, nunc vel massa montes pellentesque mattis! Dolor, ut adipiscing! Pellentesque rhoncus et! Auctor vut, vut.

### And subheadings also

Arcu porta sed, dictumst, rhoncus dignissim odio pellentesque? Turpis placerat nunc porttitor, et facilisis, dis dolor, magna, sociis integer ultricies! Odio, ac. Auctor, purus nec parturient ac quis, dignissim, magna scelerisque placerat! Et tempor porta cras ultricies nunc diam vel porttitor porttitor sed augue, scelerisque integer. Arcu, porta et, tempor. Amet placerat, auctor tortor? Odio elit auctor. Facilisis! Pellentesque, non habitasse. Nunc. Sagittis nec! Lundium, urna auctor sit. Dignissim et diam diam pellentesque aenean.

### Another subheading

Sed mus elementum urna ac ut egestas nunc enim, mauris montes! Porttitor turpis turpis, ac turpis dapibus. Placerat facilisis risus, penatibus, augue phasellus hac. Eu mus, ut montes dis enim turpis? Auctor odio massa odio aliquam? Est. Ultrices cum. Turpis. Enim natoque et enim, odio, cras turpis, in mus est? Magna non montes egestas, ac elit, in etiam etiam magna turpis nunc sed sociis purus. Parturient. Ridiculus turpis, mid turpis penatibus montes ac aenean.

## Heading YAY!

Integer parturient enim integer cras a, vel placerat pulvinar duis nunc elementum massa adipiscing, pid magnis nisi adipiscing, est augue! Et ac arcu quis! Sed porttitor, rhoncus! Enim mauris auctor. Vut placerat, in amet sociis scelerisque, rhoncus non, ridiculus lacus, integer sagittis in odio et, proin! Turpis ac amet facilisis, a enim lorem tincidunt egestas, et! Ac proin enim ultricies aliquam adipiscing! Augue amet. Dictumst lorem aliquam rhoncus porta amet! Ut pulvinar vel sit.

Ut pulvinar platea augue, eros, vel non? Odio dapibus facilisis urna, sed lorem ac purus eu porttitor? Cras porttitor, nisi, urna! Ultricies. Dictumst! Magna ut lectus lacus nunc ultrices. Aliquam cras ac? Sagittis placerat? In sed tempor ultrices nisi odio, est, aliquet cras aliquet eros, sit nec nisi ut? Tincidunt scelerisque amet porta purus. Et ac urna tortor sed. Elementum tristique nec dictumst dapibus magna et magna, ultricies, duis, dignissim turpis! Sagittis nunc aenean.

## Testing for XSS

This is a code that should not be run:

    <script type="text/javascript">
        alert("42!");
    </script>

If you see an alert in your browser, there's a XSS hole somewhere...

In elementum platea a! Integer et parturient mid proin augue, tristique mus. Pulvinar mid velit sagittis ac nunc nascetur, eros tincidunt vut ridiculus porttitor vut mid lectus risus elementum ac, rhoncus porttitor nec ut massa? A ut, magna! Arcu augue non vel nunc turpis, non lundium egestas magnis habitasse ac nec mus vel? A. Sit cursus, quis dolor pulvinar porta dapibus aenean lacus, vut et hac eros augue velit pulvinar, non cursus, pellentesque velit aliquam.

## Heading

Lectus risus? Proin ultricies ac! Urna amet vel mauris, lacus, dapibus! Ultrices, aenean turpis, enim sit cursus odio quis urna nisi? Dapibus tristique cras arcu elementum integer sit, egestas adipiscing adipiscing dapibus in turpis mid quis. Nec porta turpis turpis augue. Purus dignissim aliquet? Non, auctor sed augue, phasellus augue porttitor diam porta a augue sit vel, dis est elementum. Vel urna! Augue vel! Dictumst turpis porta platea rhoncus urna in tincidunt enim amet:

1. lorem ipsum dolor sit amet
2. excorcisomus te
3. omis legios, omnis satanicus, omis spiritus, et cetera diabolica

Massa tincidunt aliquam phasellus magna, sagittis etiam elementum et aliquam rhoncus odio magna, risus odio nisi dis nisi turpis lectus nisi, proin eros, mattis integer augue aliquam auctor parturient? Ultricies ut nunc! Aliquam, sed tortor, et proin massa tincidunt, aliquet vel, mauris magna porttitor magna sed vel! Cras tincidunt? Velit ultricies diam est cras tristique a turpis. Rhoncus phasellus, augue eros tincidunt? Dictumst. Pulvinar egestas integer lundium sed augue sit cras sed natoque! Ac.

## Another Heading

[A link to the future][1]. This is the quote:

> I am not a hero. Never was, never will be
>> Solid Snake

This is a sample code block:

    val testing = test match {
         case s: String => s.substring(0, 10)
         case _         => "No String"
    }

This is text after code.

## We need a lot of headings!

Natoque vel. Ridiculus elit non pulvinar sit, integer magna vel ultrices elit, auctor egestas lacus ac nascetur a enim arcu. Phasellus elementum odio placerat? Mauris sagittis augue tempor dapibus mattis, mid arcu rhoncus elementum eros integer, lectus urna, tortor magna risus ultricies lorem quis magna diam vel lundium. Mid, turpis nunc. Ultricies lectus placerat? Sagittis! Scelerisque? Dignissim adipiscing, lacus integer, nunc vel massa montes pellentesque mattis! Dolor, ut adipiscing! Pellentesque rhoncus et! Auctor vut, vut.

### And subheadings also

Arcu porta sed, dictumst, rhoncus dignissim odio pellentesque? Turpis placerat nunc porttitor, et facilisis, dis dolor, magna, sociis integer ultricies! Odio, ac. Auctor, purus nec parturient ac quis, dignissim, magna scelerisque placerat! Et tempor porta cras ultricies nunc diam vel porttitor porttitor sed augue, scelerisque integer. Arcu, porta et, tempor. Amet placerat, auctor tortor? Odio elit auctor. Facilisis! Pellentesque, non habitasse. Nunc. Sagittis nec! Lundium, urna auctor sit. Dignissim et diam diam pellentesque aenean.

### Another subheading

Sed mus elementum urna ac ut egestas nunc enim, mauris montes! Porttitor turpis turpis, ac turpis dapibus. Placerat facilisis risus, penatibus, augue phasellus hac. Eu mus, ut montes dis enim turpis? Auctor odio massa odio aliquam? Est. Ultrices cum. Turpis. Enim natoque et enim, odio, cras turpis, in mus est? Magna non montes egestas, ac elit, in etiam etiam magna turpis nunc sed sociis purus. Parturient. Ridiculus turpis, mid turpis penatibus montes ac aenean.

## Heading YAY!

Integer parturient enim integer cras a, vel placerat pulvinar duis nunc elementum massa adipiscing, pid magnis nisi adipiscing, est augue! Et ac arcu quis! Sed porttitor, rhoncus! Enim mauris auctor. Vut placerat, in amet sociis scelerisque, rhoncus non, ridiculus lacus, integer sagittis in odio et, proin! Turpis ac amet facilisis, a enim lorem tincidunt egestas, et! Ac proin enim ultricies aliquam adipiscing! Augue amet. Dictumst lorem aliquam rhoncus porta amet! Ut pulvinar vel sit.

Ut pulvinar platea augue, eros, vel non? Odio dapibus facilisis urna, sed lorem ac purus eu porttitor? Cras porttitor, nisi, urna! Ultricies. Dictumst! Magna ut lectus lacus nunc ultrices. Aliquam cras ac? Sagittis placerat? In sed tempor ultrices nisi odio, est, aliquet cras aliquet eros, sit nec nisi ut? Tincidunt scelerisque amet porta purus. Et ac urna tortor sed. Elementum tristique nec dictumst dapibus magna et magna, ultricies, duis, dignissim turpis! Sagittis nunc aenean.

## Testing for XSS

This is a code that should not be run:

    <script type="text/javascript">
        alert("42!");
    </script>

If you see an alert in your browser, there's a XSS hole somewhere...

  [1]: http://en.wikipedia.org/wiki/Zelda
    """
}