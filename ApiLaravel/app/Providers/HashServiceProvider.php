namespace App\Providers;

use Illuminate\Support\ServiceProvider;

class HashServiceProvider extends ServiceProvider
{
    public function register()
    {
        $this->app->singleton('hash', function () {
            return new CustomHasher();
        });
    }
}
