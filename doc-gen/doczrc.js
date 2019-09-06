const merge = require('webpack-merge');
const customCompile = {
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
            }
        ]
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js', '.jsx']
    }
};

export default {
    public: './docs/assets',
    title: 'Grails Swagger 1.0.0',
    description: 'This will allow you to create API using Grails. This is very easy',
    themeConfig: {
        colors: {
            primary: 'tomato',
        },
    },
    base: "/",
    files: '**/*.{md,markdown,mdx}',
    plugins: [],
    dest: './../docs',
    modifyBundlerConfig: config => {
        return merge(config, customCompile);
    },
    menu: [
        "Bismillah",
        {
            name: 'Installation',
            menu: []
        }
    ]
}
