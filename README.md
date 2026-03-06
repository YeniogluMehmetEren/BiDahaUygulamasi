Android Tabanlı E-Ticaret Uygulaması (BiDaha) 

Ad Soyad: Mehmet Eren Yenioğlu       *Öğrenci Numarası*: *241307023\
Üniversite: Kocaeli Üniversitesi \
Bölümü*: *Bilişim Sistemleri \
Mühendisliği*

*Ders*: *Mobil Uygulama Geliştirme*

***Özet* — Bu çalışmada, Android platformu üzerinde çalışan  bir  mobil  e-ticaret  uygulaması  geliştirilmiştir.  Proje,  Kotlin programlama  dili  ve  Android  Studio  geliştirme  ortamı kullanılarak  hayata  geçirilmiştir.  Veri  depolama  ve  kimlik doğrulama  işlemleri  için  Firebase  Realtime  Database  ve Firebase  Authentication  teknolojileri  entegre  edilmiştir. Uygulama,  "Yönetici"  (Admin)  ve  "Kullanıcı"  (User)  olmak üzere  iki  farklı  rol  tabanlı  erişim  kontrolüne  sahiptir. Yöneticiler ürün ve kullanıcı yönetimi yapabilirken, kullanıcılar kendi  ürünlerini  ekleyebilir,  ürünleri  inceleyip  sipariş verebilmektedir.** 

1. GIRIŞ

Günümüzde  mobil  cihazların  yaygınlaşmasıyla  birlikte  e- ticaret  masaüstü  platformlardan  mobil  platformlara kaymıştır. Bu projede, küçük veya orta ölçekli işletmelerin veya bireysel satıcıların ürünlerini kolayca  ekleyebileceği, yönetebileceği, müşterilerin ise hızlıca sipariş verebileceği dinamik  bir  Android  uygulaması  geliştirilmesi amaçlanmıştır. 

Uygulama, kullanıcıların güvenli bir şekilde kayıt olup giriş yapmasını,  ürünlerini  satışa  sunmasını,  başkalarının ürünlerini  arayüzde  görüntülemesini  kendi  ürünlerini düzenlemelerini  ve sepet mantığıyla sipariş oluşturmasını sağlamaktadır.  Aynı  zamanda  yönetici  yetkisine  sahip kullanıcılar,  panel  üzerinden  ürün  silme,  güncelleme işlemlerini,  kullaniciların  hesaplarını  dondurabilme  veya silme  işlemini  gerçekleştirebilmekte  ve  gelen  siparişlerin durumunu (Onaylandı/Reddedildi) değiştirebilmektedir.  

2. YÖNTEM
1. *Sistem Mimarisi ve Araçlar* 

Uygulama geliştirme sürecinde Android Studio IDE ve Kotlin  programlama  dili  tercih  edilmiştir.  Arayüz tasarımında  XML  kullanılmıştır.  Veri  tabanı  olarak NoSQL  yapısına  sahip  Firebase  Realtime  Database kullanılmıştır. Bu seçim, verilerin anlık olarak (real-time) senkronize  edilmesini  sağlamıştır.  Kullanıcıların kimliklerini  doğrulaması   için  Firebase  Authentication 

kullanilmiştir.  Bu  sayede  kullanıcıların  verileri  bulut sistemi  ile  güvenli  bir  şekilde  korunmuş,  kullanıcıların hesap  oluşturmaları  ve  hesaplarına  giriş  yapmaları sağlanmıştır. 

2. *Veritabanı Tasarımı* 

Projede  ilişkisel  veri  bütünlüğünü  sağlamak  adına “Users”, “Admins”, “Products”, “Carts” ve “Orders” olmak üzere beş ana düğüm tasarlanmıştır.  “Users” ve “Admins” kullanıcıların isim, soyisim, email, kullanıcı id, hesabın aktif olup  olmadığı  ve  rol  bilgilerini,  “Products”  ürünlerin açıklamasi,  resimi,  satıcı  id,  ürün  id  ve  fiyat  detaylarını, “Carts” hangi kullanıcıların hangi ürünleri sepetine eklediğini, “Orders”  ise  sipariş  veren  kullanıcıların  eposta  ve  idsi, siparişlerin  toplam  fiyatını  ve  siparişlerin   durumunu tutmaktadır. 

![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.001.png)

Şekil 1: Veritabanı ERD Diyagramı 

3. *Algoritma ve İş Akışı* 

Kullanıcı  "Sepeti  Onayla"  butonuna  bastığında  tetiklenen süreç Şekil 2'de gösterilmiştir. 

![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.002.png)

Şekil 2: Sipariş Oluşturma Akış Şeması 

4. *API Entegrasyonu* 

Uygulamaya Retrofit 2 kütüphanesi ile, anlık kur bilgilerini ücretsiz ve açık kaynaklı bir servis olan *ExchangeRate-API sayesinde* anlık döviz kuru takibi özelliği entegre edilmiştir. Bu  özellik  sayesinde,  kullanıcı  ürün  detay  sayfasına girdiğinde, uygulama API'ye istek atar. Gelen yanıttan "1 TL =  X  USD"  dönüşüm  oranı  çekilir  ve  matematiksel  işlem yapılarak  ürünün  Dolar  cinsinden  fiyatı  dinamik  olarak TextView üzerinde gösterilir. 

![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.003.png)

Şekil 3: Ürün Detay Ekranı 

3. DENEYSEL SONUÇLAR

Geliştirilen uygulama Android  emülatörlerde (Medium Phone,  Pixel  9  Pro)  ve  fiziksel  cihazlarda  (Xiaomi)  test edilmiştir. 

1. *Kimlik Doğrulama* 

Uygulamanın  giriş  modülü,  Firebase  Authentication servisi  ile  sağlanmıştır.  Yapılan  testlerde,  hatalı  e-posta formatı  veya  eksik  şifre  girilmesi  durumunda  sistemin kullanıcıyı uyardığı ve girişi engellediği doğrulanmıştır. 

![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.004.png)![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.005.png)Şekil 3 

ve 4: Giriş Ekranı ve Hatalı Girilen Bilgiler 

2. *Ürün Listeleme* 

Kullanıcılar  tarafından  giriş  yapıldığında,  anasayfada ürünlerin  RecyclerView  bileşeni  kullanılarak  listelendiği "Keşfet"  ekranı  görüntülenmektedir.  Arama  çubuğu kullanılarak  ürünlerin  isimlerine  göre  arama gerçekleştirilebilir. Şekil 5'de görüldüğü üzere, ürünler kart yapısında,  fiyat,  isim  ve  resim  ile  birlikte  kullanıcıya sunulmaktadır. 

![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.006.png) ![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.007.png)

Şekil 5 ve 6: Keşfet Ekranı 

3. *Ürün Ekleme* 

Yeni ürün oluşturma arayüzü, metinsel verilerin yanı sıra görsel  medya  yönetimini  de  destekleyecek  şekilde tasarlanmıştır. Bu ekranda kullanıcı; ürün adı, fiyatı ve ürün açıklaması gibi temel verileri EditText bileşenleri aracılığıyla sisteme  girmektedir.  Uygulamanın  en  önemli yeteneklerinden  biri  cihazın  yerel  depolama  birimiyle etkileşime  girebilmesidir.  Kullanıcı  ürün  görseli  alanına tıkladığında telefon hafızasından dilediği fotoğrafı seçebilir, ilgili görselin URI adresi uygulama tarafından yakalanmakta ve  önizleme  olarak  ekrana  yansıtılmaktadır.  "Kaydet" butonuna basıldığında, girilen tüm metinsel veriler ve seçilen görsel  Firebase  Realtime  Database  üzerindeki  Products düğümüne kaydedilmektedir. 

![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.008.png)

Şekil 7: Ürün Ekleme Arayüzü 

4. *Sepet Yönetimi* 

Uygulamanın  e-ticaret  fonksiyonlarının  testinde, kullanıcıların ürünleri sepete ekleyip çıkartabildiği ve anlık olarak toplam tutarın hesaplandığı görülmüştür. "Sipariş Ver" butonuna  tıklandığında  sistem  siparişi  yöneticilerin  onay vermesi için beklettiği görülmüştür. 

![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.009.png)![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.010.png)

Şekil 8 ve 9: Sepet Ekranı ve Onay Ekranı 

5. *Yönetici Paneli* 

Admin  yetkisine  sahip  bir  hesapla  giriş  yapıldığında,  alt navigasyon  çubuğunda  standart  kullanıcılarda  görünmeyen "Admin  Paneli"  sekmesi  aktif  hale  gelmektedir.  Bu  panel üzerinden  kullanıcı  hesaplarını  yönetme,  ilanları  yönetme, yeni  kullanıcı/yönetici  hesabı  oluşturma  ve  bekleyen siparişleri  onaylama/reddetme  işlemleri  başarıyla gerçekleştirilmiştir. 

![](Aspose.Words.cf6714e6-6cef-471e-a842-909c1aaddcbb.011.png)

Şekil 10: Yönetici Paneli 

4. SONUÇ

Bu  proje  çalışması  kapsamında,  mobil  yazılım  geliştirme ekosisteminin temelleri atılmış; kullanıcı deneyimi tasarımı, üçüncü parti servis entegrasyonları, rol tabanlı yetkilendirme ve donanım/medya erişimi konularında kapsamlı yetkinlikler kazanılmıştır. 

Geliştirme  sürecinde  karşılaşılan  en  önemli  zorluk,  veri depolama yöntemi seçiminde yaşanmıştır. Projenin başlangıç aşamasında,  kullanıcı  verilerini  saklamak  için  Android'in yerel  depolama  birimi  olan  **SharedPreferences**  yapısı kullanılmıştır.  Ancak  SharedPreferences'ın  verileri  sadece yerel cihazda (Local) tuttuğu test aşamasında fark edilmiştir. Projenin  çok  kullanıcılı  ve  bulut  tabanlı  yapısı  gereği verilerin  merkezi  bir  sunucuda  tutulması  zorunluluğu doğduğundan, Firebase Realtime Database çözümüne geçiş yapılmıştır. Bu süreç zaman kaybı oluşturmuştur. 

Bunun  yanı  sıra,  Firebase  entegrasyonu  sırasında  ağ işlemlerinden  kaynaklı  asenkron  veri  çekme  sorunları yaşanmıştır.  Verilerin  internet  hızına  bağlı  olarak  geç gelmesi, arayüzde donmalara veya  uygulamada çökmelere neden  olmuştur.  Bu  problem,  Firebase  platformunun sağladığı  addOnSuccessListener  mekanizması  kullanılarak çözülmüştür. 

GitHub bağlantısı: [https://github.com/YeniogluMehmetEren/BiDahaUygulamas i ](https://github.com/YeniogluMehmetEren/BiDahaUygulamasi)

GoogleDrive bağlantısı: [https://drive.google.com/drive/folders/1kXmIbe8RWCSuAii EbcW7-pSRn-NaReF-?usp=sharing ](https://drive.google.com/drive/folders/1kXmIbe8RWCSuAiiEbcW7-pSRn-NaReF-?usp=sharing)

5. KAYNAKÇA
1. Android Developers, "Button - Material Design 3 

(Compose)," *Android Developers Documentation*, 2025. [Çevrimiçi]. Erişim: \
[https://developer.android.com/develop/ui/compose/compone nts/button?hl=tr ](https://developer.android.com/develop/ui/compose/components/button?hl=tr)

2. Android Developers, "Uygulamanızda hata ayıklama (Debug your app)," *Android Studio User Guide*, 2025. [Çevrimiçi]. Erişim: [https://developer.android.com/studio/debug?hl=tr ](https://developer.android.com/studio/debug?hl=tr)
3. Android Developers, "Uygulamanızın kullanıcı arayüzü 

kaynaklarını yönetme (Resource Manager)," *Android Studio User Guide*, 2025. [Çevrimiçi]. Erişim: \
[https://developer.android.com/studio/write/resource-](https://developer.android.com/studio/write/resource-manager?hl=tr#import)\
[manager?hl=tr#import ](https://developer.android.com/studio/write/resource-manager?hl=tr#import)

4. Indently, "Saving data with Shared Preferences in Android Studio (Kotlin 2020)", YouTube, 2020. [Çevrimiçi]. Erişim: [https://www.youtube.com/watch?v=S5uLAGnBvUY ](https://www.youtube.com/watch?v=S5uLAGnBvUY)
5. Kotlin, "MutableList - Kotlin Standard Library," *Kotlin API Reference*, 2025. [Çevrimiçi]. Erişim: [https://kotlinlang.org/api/core/kotlin- stdlib/kotlin.collections/-mutable-list ](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-mutable-list)
5. Insane Developer, "Modern Tab Layout With Fragments In Kotlin | Android UI", YouTube, 2020. [Çevrimiçi]. Erişim: [https://www.youtube.com/watch?v=LoSf9vHRnxg&t=827s ](https://www.youtube.com/watch?v=LoSf9vHRnxg&t=827s)

   7. Kotlin, "AlertDialog - Androidx.compose.material3," *Kotlin API Reference*, 2025. [Çevrimiçi]. Erişim: [https://kotlinlang.org/api/compose-](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-alert-dialog.html)

[multiplatform/material3/androidx.compose.material3/-alert- dialog.html ](https://kotlinlang.org/api/compose-multiplatform/material3/androidx.compose.material3/-alert-dialog.html)

8. Google Firebase, "Android'de Firebase Realtime 

Database'i Kullanmaya Başlama," *Firebase Documentation*, 2025. [Çevrimiçi]. Erişim: 

[https://firebase.google.com/docs/database/android/start?hl=t](https://firebase.google.com/docs/database/android/start?hl=tr)

[r ](https://firebase.google.com/docs/database/android/start?hl=tr)

9. Google Firebase, "Android'de Parolalarla Firebase Kimlik Doğrulaması (Password Auth)," *Firebase Documentation*, 2025. [Çevrimiçi]. Erişim: 

[https://firebase.google.com/docs/auth/android/password- auth?hl=tr ](https://firebase.google.com/docs/auth/android/password-auth?hl=tr)

10. Google Firebase, "Kullanıcıları Yönetme (Android) - Profili Güncelleme," *Firebase Documentation*, 2025. [Çevrimiçi]. Erişim: [https://firebase.google.com/docs/auth/android/manage- users?hl=tr#update_a_users_profile ](https://firebase.google.com/docs/auth/android/manage-users?hl=tr#update_a_users_profile)


